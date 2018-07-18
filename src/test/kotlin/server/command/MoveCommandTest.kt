package server.command


import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

internal class MoveCommandTest {

    @ParameterizedTest
    @MethodSource("validMoveCommandTestDataProvider")
    fun `execute move commands`(testData: ValidMoveCommandTestData) {
        testData.command.execute()
        assertTrue(testData.expectedPoint == initialGame.gameState.player.getLocation())
    }

    companion object {
        private val initialGame = Game(GameState(player =
            Player(playerState =
                PlayerState(location = Point(2, 2)))))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, MoveNorthCommand(initialGame), Point(3, 2)),
            ValidMoveCommandTestData(initialGame, MoveEastCommand(initialGame), Point(3, 3)),
            ValidMoveCommandTestData(initialGame, MoveSouthCommand(initialGame), Point(2, 3)),
            ValidMoveCommandTestData(initialGame, MoveWestCommand(initialGame), Point(2, 2))
        )
    }
}

data class ValidMoveCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedPoint: Point
)