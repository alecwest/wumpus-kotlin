package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import game.world.RoomContent
import game.world.World
import java.awt.Point
import kotlin.test.assertFalse

class HereEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add glitter to current room as result of gold`() {
        world.addRoomContent(point, RoomContent.GOLD)
        assertTrue(world.hasRoomContent(point, RoomContent.GLITTER))
    }

    @Test
    fun `remove glitter from current room as result of gold removal`() {
        val world = Helpers.createWorld(roomContent = mapOf(point to arrayListOf(RoomContent.GOLD)))
        world.removeRoomContent(point, RoomContent.GOLD)
        assertFalse(world.hasRoomContent(point, RoomContent.GLITTER))
    }
}