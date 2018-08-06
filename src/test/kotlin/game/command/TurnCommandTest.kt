package game.command

import game.Game
import game.world.Dangerous1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

internal class TurnCommandTest {
    @Test
    fun `turn commands are equal`() {
        val command = TurnCommand(Direction.EAST)
        assertEquals(command, command)
        assertNotEquals(command, Direction.EAST)
        assertNotEquals(TurnCommand(Direction.EAST), TurnCommand(Direction.WEST))
        assertEquals(command, TurnCommand(Direction.EAST))
    }

    @ParameterizedTest
    @MethodSource("validTurnCommandTestDataProvider")
    fun `execute turn commands`(testData: ValidTurnCommandTestData) {
        testData.command.setGame(testData.givenGame)
        testData.command.execute()
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())
        // Verify the rest of the player state is maintained
        assertEquals(Point(2, 2), testData.givenGame.getPlayerLocation())
    }

    companion object {
        private val initialPlayer = Helpers.createPlayer(location = Point(2, 2), facing = Direction.SOUTH)
        private val initialWorld = Helpers.createWorld(roomContent = mapOf(Point(3, 2) to arrayListOf(Dangerous1.PIT)))
        private val initialGame = Helpers.createGame(player = initialPlayer, world = initialWorld)

        @JvmStatic
        fun validTurnCommandTestDataProvider() = Stream.of(
                ValidTurnCommandTestData(initialGame, TurnCommand(Direction.EAST), CommandResult(arrayListOf(),
                        initialPlayer.getPlayerState().copyThis(facing = Direction.EAST), initialGame.getRoomContent())),
                ValidTurnCommandTestData(initialGame, TurnCommand(Direction.NORTH), CommandResult(arrayListOf(),
                        initialPlayer.getPlayerState().copyThis(facing = Direction.NORTH), initialGame.getRoomContent())),
                ValidTurnCommandTestData(initialGame, TurnCommand(Direction.EAST), CommandResult(arrayListOf(),
                        initialPlayer.getPlayerState().copyThis(facing = Direction.EAST), initialGame.getRoomContent())),
                ValidTurnCommandTestData(initialGame, TurnCommand(Direction.SOUTH), CommandResult(arrayListOf(),
                        initialPlayer.getPlayerState().copyThis(facing = Direction.SOUTH), initialGame.getRoomContent()))
        )
    }
}

data class ValidTurnCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedCommandResult: CommandResult
)