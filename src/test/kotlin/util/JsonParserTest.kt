package util

import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertTrue
import world.RoomContent
import java.io.StringReader

class JsonParserTest {
    @Test
    fun `test create world from json file`() {
        val world = JsonParser.buildFromJsonFile("src/test/resources/testFile.json")
        assertTrue(world.hasRoomContent(Point(3, 5), RoomContent.GOLD))
        assertTrue(world.hasRoomContent(Point(3, 5), RoomContent.SUPMUW_EVIL))
        assertTrue(world.hasRoomContent(Point(10, 9), RoomContent.PIT))
    }

    @Test
    fun `test create world from json string`() {
        val world = JsonParser.buildFromJson(StringReader("""{
            |"world-size": 4,
            |"data": [
            |   {
            |       "x": 3,
            |       "y": 5,
            |       "content": "G"
            |   }
            |]
            |}""".trimMargin()))
        assertTrue(world.hasRoomContent(Point(3, 5), RoomContent.GOLD))
    }
}