package server.command

import game.Game
import game.GameState
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import game.world.Room
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.api.Assertions.assertEquals
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
        assertEquals(testData.expectedInventory.inventoryItems,
                initialGame.gameState.player.getInventory())
        // Verify the rest of the player state is maintained
        assertEquals(Direction.SOUTH, initialGame.gameState.player.getDirection())
    }

    companion object {
        private val initialGame = Game(GameState(world = World(roomsToAdd =
            mapOf(Point(2, 2) to Room(arrayListOf(RoomContent.GOLD, RoomContent.FOOD)))),
            player = Player(playerState =
            PlayerState(location = Point(2, 2), facing = Direction.SOUTH,
            inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 2))))))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validGrabCommandTestDataProvider() = Stream.of(
                ValidGrabCommandTestData(initialGame, GrabFoodCommand(initialGame), PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1))),
                ValidGrabCommandTestData(initialGame, GrabFoodCommand(initialGame), PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 2))),
                ValidGrabCommandTestData(initialGame, GrabGoldCommand(initialGame), PlayerInventory(mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 2, InventoryItem.GOLD to 1)))
        )
    }
}

data class ValidGrabCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedInventory: PlayerInventory
)