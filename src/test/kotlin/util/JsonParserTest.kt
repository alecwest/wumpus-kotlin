package util

import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertTrue
import game.world.RoomContent

class JsonParserTest {
    @Test
    fun `test create world from json file`() {
        val world = JsonParser.buildFromJsonFile("src/test/resources/testFile.json")
        assertTrue(world.hasRoomContent(Point(3, 5), RoomContent.GOLD))
        assertTrue(world.hasRoomContent(Point(3, 5), RoomContent.SUPMUW_EVIL))
        assertTrue(world.hasRoomContent(Point(10, 9), RoomContent.PIT))
    }
}