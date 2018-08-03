package game.command


import game.Game
import game.player.InventoryItem
import game.world.Perception
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import util.right
import java.awt.Point
import java.util.stream.Stream

internal class MoveCommandTest {

    @ParameterizedTest
    @MethodSource("validMoveCommandTestDataProvider")
    fun `execute move command and turn right`(testData: ValidMoveCommandTestData) {
        assertEquals(testData.expectedStartingDirection, testData.givenGame.getPlayerDirection())
        testData.command.setGame(testData.givenGame)
        testData.command.execute()
        assertEquals(testData.expectedCommandResult.getPlayerState().getLocation(), testData.givenGame.getPlayerLocation())
        turnRight(testData.givenGame)
        // Verify the rest of the player state is maintained
        assertEquals(mapOf(InventoryItem.ARROW to 2), testData.givenGame.getPlayerInventory())
    }

    private fun turnRight(game: Game) {
        val command = TurnCommand(game.getPlayerDirection().right())
        command.setGame(game)
        command.execute()
    }

    companion object {
        private val initialPlayer = Helpers.createPlayer(
                location = Point(2, 2), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2))
        private val initialGame = Helpers.createGame(player = initialPlayer)
        private val initialPlayerInCorner = Helpers.createPlayer(
                location = Point(0, 0), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2))
        private val initialPlayerInCornerWorld = Helpers.createWorld(
                roomContent = mapOf(
                        Point(0, 1) to arrayListOf(RoomContent.BLOCKADE),
                        Point(1, 0) to arrayListOf(RoomContent.GOLD)))
        private val playerInCornerGame = Helpers.createGame(player = initialPlayerInCorner, world = initialPlayerInCornerWorld)

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, Direction.SOUTH, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(2, 1)))),
            ValidMoveCommandTestData(initialGame, Direction.WEST, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(1, 1)))),
            ValidMoveCommandTestData(initialGame, Direction.NORTH, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(1, 2)))),
            ValidMoveCommandTestData(initialGame, Direction.EAST, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(2, 2)))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.SOUTH, MoveCommand(),
                    CommandResult(arrayListOf(Perception.WALL_BUMP), initialPlayer.getPlayerState().copyThis(
                            location = Point(0, 0)))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.WEST, MoveCommand(),
                    CommandResult(arrayListOf(Perception.WALL_BUMP), initialPlayer.getPlayerState().copyThis(
                            location = Point(0, 0)))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.NORTH, MoveCommand(),
                    CommandResult(arrayListOf(Perception.BLOCKADE_BUMP), initialPlayer.getPlayerState().copyThis(
                            location = Point(0, 0)))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.EAST, MoveCommand(),
                    CommandResult(arrayListOf(Perception.GLITTER), initialPlayer.getPlayerState().copyThis(
                            location = Point(1, 0))))
        )
    }
}

data class ValidMoveCommandTestData (
        val givenGame: Game,
        val expectedStartingDirection: Direction,
        val command: Command,
        val expectedCommandResult: CommandResult
)