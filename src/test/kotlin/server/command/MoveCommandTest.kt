package server.command


import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import server.command.move.MoveEastCommand
import server.command.move.MoveNorthCommand
import server.command.move.MoveSouthCommand
import server.command.move.MoveWestCommand
import util.Direction
import java.awt.Point
import java.util.stream.Stream

internal class MoveCommandTest {

    @ParameterizedTest
    @MethodSource("validMoveCommandTestDataProvider")
    fun `execute move commands`(testData: ValidMoveCommandTestData) {
        testData.command.execute()
        assertEquals(testData.expectedPoint, testData.givenGame.getPlayerLocation())
        // Verify the rest of the player state is maintained
        assertEquals(Direction.SOUTH, testData.givenGame.getPlayerDirection())
    }

    companion object {
        private val initialGame = Game(GameState(player =
            Player(playerState =
                PlayerState(location = Point(2, 2), facing = Direction.SOUTH))))
        private val playerInCornerGame = Helpers.createGame(player = Helpers.createPlayer(location = Point(0, 0)))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, MoveNorthCommand(initialGame), Point(3, 2)),
            ValidMoveCommandTestData(initialGame, MoveEastCommand(initialGame), Point(3, 3)),
            ValidMoveCommandTestData(initialGame, MoveSouthCommand(initialGame), Point(2, 3)),
            ValidMoveCommandTestData(initialGame, MoveWestCommand(initialGame), Point(2, 2)),
            ValidMoveCommandTestData(playerInCornerGame, MoveSouthCommand(playerInCornerGame), Point(0, 0)),
            ValidMoveCommandTestData(playerInCornerGame, MoveWestCommand(playerInCornerGame), Point(0, 0))
        )
    }
}

data class ValidMoveCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedPoint: Point
)