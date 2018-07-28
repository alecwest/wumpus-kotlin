package server.command


import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import server.command.move.*
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
        private val playerInCornerGame = Helpers.createGame(player = Helpers.createPlayer(
                location = Point(0, 0), facing = Direction.SOUTH))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame, Direction.NORTH), Point(2, 3)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame, Direction.EAST), Point(3, 3)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame, Direction.SOUTH), Point(3, 2)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame, Direction.WEST), Point(2, 2)),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(playerInCornerGame, Direction.SOUTH), Point(0, 0)),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(playerInCornerGame, Direction.WEST), Point(0, 0))
        )
    }
}

data class ValidMoveCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedPoint: Point
)