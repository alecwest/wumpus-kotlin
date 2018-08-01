package game.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.server.Server
import game.world.Perception
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

class GrabCommandTest {
    @ParameterizedTest
    @MethodSource("validGrabCommandTestDataProvider")
    fun `execute grab command`(testData: ValidGrabCommandTestData) {
        testData.command.setGame(testData.givenGame)
        assertEquals(testData.command.game, testData.givenGame)

        testData.command.execute()
        assertEquals(testData.expectedInventory.getInventory(),
                testData.givenGame.getPlayerInventory())
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())

        // Verify the rest of the player state is maintained
        assertEquals(Direction.SOUTH, testData.givenGame.getPlayerDirection())
        assertFalse(testData.givenGame.getWorld().hasRoomContent(testPoint, testData.lostWorldContent))
    }

    companion object {
        private val testPoint = Point(2, 2)
        private val initialGame = Helpers.createGame(
                world = Helpers.createWorld(
                        roomContent = mapOf(
                                Point(2, 2) to arrayListOf(RoomContent.GOLD, RoomContent.SUPMUW, RoomContent.ARROW))),
                player = Helpers.createPlayer(
                        location = testPoint,
                        facing = Direction.SOUTH,
                        inventoryContent = mapOf(InventoryItem.ARROW to 2)))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validGrabCommandTestDataProvider() = Stream.of(
                ValidGrabCommandTestData(initialGame, GrabCommand(InventoryItem.FOOD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1)),
                        RoomContent.FOOD, CommandResult(arrayListOf(Perception.GLITTER))),
                ValidGrabCommandTestData(initialGame, GrabCommand(InventoryItem.FOOD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1)),
                        RoomContent.FOOD, CommandResult(arrayListOf(Perception.GLITTER))),
                ValidGrabCommandTestData(initialGame, GrabCommand(InventoryItem.GOLD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2,
                                InventoryItem.FOOD to 1, InventoryItem.GOLD to 1)),
                        RoomContent.GOLD, CommandResult(arrayListOf())),
                ValidGrabCommandTestData(initialGame, GrabCommand(InventoryItem.ARROW),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 3,
                                InventoryItem.FOOD to 1, InventoryItem.GOLD to 1)),
                        RoomContent.GOLD, CommandResult(arrayListOf()))
        )
    }
}

data class ValidGrabCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedInventory: PlayerInventory,
    val lostWorldContent: RoomContent,
    val expectedCommandResult: CommandResult
)