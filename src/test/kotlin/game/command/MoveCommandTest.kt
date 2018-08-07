package game.command


import game.Game
import game.player.InventoryItem
import game.world.Perception
import game.world.GameObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import util.right
import java.awt.Point
import java.util.stream.Stream

internal class MoveCommandTest {
    @Test
    fun `move commands are equal`() {
        val command = MoveCommand()
        assertEquals(command, command)
        assertNotEquals(command, Direction.EAST)
        assertEquals(command, MoveCommand())
    }

    @ParameterizedTest
    @MethodSource("validMoveCommandTestDataProvider")
    fun `execute move command and turn right`(testData: ValidMoveCommandTestData) {
        testData.command.setGame(testData.givenGame)
        testData.command.execute()
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())
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
                location = Point(2, 4), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2))
        private val initialGame = Helpers.createGame(player = initialPlayer)
        private val initialPlayerInCorner = Helpers.createPlayer(
                location = Point(0, 0), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2))
        private val initialPlayerInCornerWorld = Helpers.createWorld(
                gameObject = mapOf(
                        Point(0, 1) to arrayListOf(GameObject.BLOCKADE),
                        Point(1, 0) to arrayListOf(GameObject.GOLD)))
        private val playerInCornerGame = Helpers.createGame(player = initialPlayerInCorner, world = initialPlayerInCornerWorld)

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, MoveCommand(),
                    CommandResult(arrayListOf(),
                            initialPlayer.getPlayerState().copyThis(location = Point(2, 3), facing = Direction.SOUTH),
                            arrayListOf(GameObject.BREEZE))),
            ValidMoveCommandTestData(initialGame, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(1, 3), facing = Direction.WEST))),
            ValidMoveCommandTestData(initialGame, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(1, 4), facing = Direction.NORTH))),
            ValidMoveCommandTestData(initialGame, MoveCommand(),
                    CommandResult(playerState = initialPlayer.getPlayerState().copyThis(location = Point(2, 4), facing = Direction.EAST))),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(),
                    CommandResult(arrayListOf(Perception.WALL_BUMP), initialPlayerInCorner.getPlayerState().copyThis(
                            location = Point(0, 0), facing = Direction.SOUTH))),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(),
                    CommandResult(arrayListOf(Perception.WALL_BUMP), initialPlayerInCorner.getPlayerState().copyThis(
                            location = Point(0, 0), facing = Direction.WEST))),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(),
                    CommandResult(arrayListOf(Perception.BLOCKADE_BUMP), initialPlayerInCorner.getPlayerState().copyThis(
                            location = Point(0, 0), facing = Direction.NORTH))),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(),
                    CommandResult(arrayListOf(), initialPlayerInCorner.getPlayerState().copyThis(
                            location = Point(1, 0), facing = Direction.EAST),
                            initialPlayerInCornerWorld.getGameObject(Point(1, 0))))
        )
    }
}

data class ValidMoveCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedCommandResult: CommandResult
)