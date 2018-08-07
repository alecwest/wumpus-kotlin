package util

import game.Game
import game.player.InventoryItem
import org.junit.jupiter.api.Test
import java.awt.Point
import game.world.GameObject
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class JsonParserTest {
    @Test
    fun `test create world from json file`() {
        val jsonParser = JsonParser()
        assertEquals(jsonParser::class, JsonParser::class)
        val game: Game = JsonParser.buildFromJsonFile(Helpers.worldFileName)
        assertTrue(game.hasGameObject(Point(3, 5), GameObject.GOLD))
        assertTrue(game.hasGameObject(Point(3, 5), GameObject.SUPMUW_EVIL))
        assertTrue(game.hasGameObject(Point(10, 9), GameObject.PIT))
        assertEquals(game.getPlayerDirection(), Direction.EAST)
        assertEquals(game.getPlayerLocation(), Point(3, 7))
        assertEquals(game.getPlayerInventory(), mapOf(InventoryItem.ARROW to 2, InventoryItem.FOOD to 1))
    }
}