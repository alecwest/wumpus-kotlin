package server.command

import game.Game
import game.GameState
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import game.world.RoomContent
import game.world.World
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
        testData.command.execute()
        assertEquals(testData.expectedInventory.getInventory(),
                initialGame.getPlayerInventory())
        // Verify the rest of the player state is maintained
        assertEquals(Direction.SOUTH, initialGame.getPlayerDirection())
        assertFalse(initialGame.getWorld().hasRoomContent(testPoint, testData.lostWorldContent))
    }

    companion object {
        private val testPoint = Point(2, 2)
        private val initialGame = Game(GameState(world = getInitialRooms(),
            player = Player(playerState =
            PlayerState(location = testPoint, facing = Direction.SOUTH,
            inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 2))))))

        private fun getInitialRooms(): World {
            val world = World()
            for (content in arrayListOf(RoomContent.GOLD, RoomContent.FOOD)) {
                world.addRoomContent(testPoint, content)
            }
            return world
        }

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validGrabCommandTestDataProvider() = Stream.of(
                ValidGrabCommandTestData(initialGame, GrabCommand(initialGame, InventoryItem.FOOD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1)), RoomContent.FOOD),
                ValidGrabCommandTestData(initialGame, GrabCommand(initialGame, InventoryItem.FOOD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1)), RoomContent.FOOD),
                ValidGrabCommandTestData(initialGame, GrabCommand(initialGame, InventoryItem.GOLD),
                        PlayerInventory(mapOf(InventoryItem.ARROW to 2,
                                InventoryItem.FOOD to 1, InventoryItem.GOLD to 1)), RoomContent.GOLD)
        )
    }
}

data class ValidGrabCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedInventory: PlayerInventory,
    val lostWorldContent: RoomContent
)