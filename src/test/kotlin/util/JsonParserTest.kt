package util

import game.Game
import game.player.InventoryItem
import org.junit.jupiter.api.Test
import java.awt.Point
import game.world.RoomContent
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class JsonParserTest {
    @Test
    fun `test create world from json file`() {
        val jsonParser = JsonParser()
        val game: Game = JsonParser.buildFromJsonFile("src/test/resources/testFile.json")
        assertTrue(game.hasRoomContent(Point(3, 5), RoomContent.GOLD))
        assertTrue(game.hasRoomContent(Point(3, 5), RoomContent.SUPMUW_EVIL))
        assertTrue(game.hasRoomContent(Point(10, 9), RoomContent.PIT))
        assertEquals(game.getPlayerDirection(), Direction.EAST)
        assertEquals(game.getPlayerLocation(), Point(3, 7))
        assertEquals(game.getPlayerInventory(), mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1))
    }
}