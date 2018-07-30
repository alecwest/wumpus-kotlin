package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.RoomContent
import game.world.World
import java.awt.Point
import kotlin.test.assertFalse

class AdjacentEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add breeze to adjacent rooms as effect of pit`() {
        world.addRoomContent(point, RoomContent.PIT)
        for (point in point.adjacents()) {
            assertTrue(world.hasRoomContent(point, RoomContent.BREEZE))
        }
        for (point in point.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }

    @Test
    fun `remove breeze from adjacent rooms as result of pit removal`() {
        val world = Helpers.createWorld(roomContent = mapOf(point to arrayListOf(RoomContent.PIT)))
        world.removeRoomContent(point, RoomContent.PIT)
        for (point in point.adjacents() + point.diagonals()) {
            assertFalse(world.hasRoomContent(point, RoomContent.BREEZE))
        }
    }
}