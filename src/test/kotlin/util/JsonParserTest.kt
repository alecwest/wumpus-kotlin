package util

import game.Game
import game.player.InventoryItem
import org.junit.jupiter.api.Test
import java.awt.Point
import game.world.RoomContent
import game.world.Dangerous1
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class JsonParserTest {
    @Test
    fun `test create world from json file`() {
        val jsonParser = JsonParser()
        assertEquals(jsonParser::class, JsonParser::class)
        val game: Game = JsonParser.buildFromJsonFile(Helpers.worldFileName)
        assertTrue(game.hasRoomContent(Point(3, 5), RoomContent.GOLD))
        assertTrue(game.hasRoomContent(Point(3, 5), Dangerous1.SUPMUW_EVIL))
        assertTrue(game.hasRoomContent(Point(10, 9), Dangerous1.PIT))
        assertEquals(game.getPlayerDirection(), Direction.EAST)
        assertEquals(game.getPlayerLocation(), Point(3, 7))
        assertEquals(game.getPlayerInventory(), mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1))
    }
}