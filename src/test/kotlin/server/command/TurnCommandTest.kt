package server.command

import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

internal class TurnCommandTest {
    @ParameterizedTest
    @MethodSource("validTurnCommandTestDataProvider")
    fun `execute turn commands`(testData: ValidTurnCommandTestData) {
        testData.command.execute()
        assertEquals(testData.expectedDirection, testData.givenGame.getPlayerDirection())
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())
        // Verify the rest of the player state is maintained
        assertEquals(Point(2, 2), testData.givenGame.getPlayerLocation())
    }

    companion object {
        private val initialGame = Helpers.createGame(player =
        Helpers.createPlayer(location = Point(2, 2), facing = Direction.SOUTH),
                world = Helpers.createWorld(roomContent = mapOf(Point(3, 2) to RoomContent.PIT)))

        @JvmStatic
        fun validTurnCommandTestDataProvider() = Stream.of(
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.EAST), Direction.EAST, CommandResult()),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.NORTH), Direction.NORTH, CommandResult()),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.EAST), Direction.EAST, CommandResult()),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.SOUTH), Direction.SOUTH, CommandResult())
        )
    }
}

data class ValidTurnCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedDirection: Direction,
        val expectedCommandResult: CommandResult
)