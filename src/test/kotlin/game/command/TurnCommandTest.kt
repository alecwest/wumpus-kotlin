package game.command

import game.Game
import game.player.PlayerState
import game.world.Perception
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
        testData.command.setGame(testData.givenGame)
        testData.command.execute()
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())
        // Verify the rest of the player state is maintained
        assertEquals(Point(2, 2), testData.givenGame.getPlayerLocation())
    }

    companion object {
        private val initialPlayer = Helpers.createPlayer(location = Point(2, 2), facing = Direction.SOUTH)
        private val initialWorld = Helpers.createWorld(roomContent = mapOf(Point(3, 2) to arrayListOf(RoomContent.PIT)))
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