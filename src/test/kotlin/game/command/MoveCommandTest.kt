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
        assertEquals(testData.expectedPoint, testData.givenGame.getPlayerLocation())
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
        private val initialGame = Helpers.createGame(player = Helpers.createPlayer(
                location = Point(2, 2), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2)))
        private val playerInCornerGame = Helpers.createGame(player = Helpers.createPlayer(
                location = Point(0, 0), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2)),
                world = Helpers.createWorld(
                        roomContent = mapOf(
                                Point(0, 1) to arrayListOf(RoomContent.BLOCKADE),
                                Point(1, 0) to arrayListOf(RoomContent.GOLD))))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, Direction.SOUTH, MoveCommand(), Point(2, 1), CommandResult()),
            ValidMoveCommandTestData(initialGame, Direction.WEST, MoveCommand(), Point(1, 1), CommandResult()),
            ValidMoveCommandTestData(initialGame, Direction.NORTH, MoveCommand(), Point(1, 2), CommandResult()),
            ValidMoveCommandTestData(initialGame, Direction.EAST, MoveCommand(), Point(2, 2), CommandResult()),
            ValidMoveCommandTestData(playerInCornerGame, Direction.SOUTH, MoveCommand(), Point(0, 0), CommandResult(arrayListOf(Perception.WALL_BUMP))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.WEST, MoveCommand(), Point(0, 0), CommandResult(arrayListOf(Perception.WALL_BUMP))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.NORTH, MoveCommand(), Point(0, 0), CommandResult(arrayListOf(Perception.BLOCKADE_BUMP))),
            ValidMoveCommandTestData(playerInCornerGame, Direction.EAST, MoveCommand(), Point(1, 0), CommandResult(arrayListOf(Perception.GLITTER)))
        )
    }
}

data class ValidMoveCommandTestData (
        val givenGame: Game,
        val expectedStartingDirection: Direction,
        val command: Command,
        val expectedPoint: Point,
        val expectedCommandResult: CommandResult
)