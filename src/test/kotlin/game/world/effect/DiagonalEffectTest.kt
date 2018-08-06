package game.world.effect

import game.world.Dangerous1
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.RoomContent
import game.world.World
import java.awt.Point
import kotlin.test.assertFalse

class DiagonalEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add moo to surrounding rooms as effect of supmuw`() {
        world.addRoomContent(point, Dangerous1.SUPMUW)
        for (point in point.adjacents() + point.diagonals()) {
            assertTrue(world.hasRoomContent(point, RoomContent.MOO))
        }
    }

    @Test
    fun `remove moo from surrounding rooms as result of supmuw removal`() {
        val world = Helpers.createWorld(roomContent = mapOf(point to arrayListOf(Dangerous1.SUPMUW)))
        world.removeRoomContent(point, Dangerous1.SUPMUW)
        for (point in point.adjacents() + point.diagonals()) {
            assertFalse(world.hasRoomContent(point, RoomContent.MOO))
        }
    }
}