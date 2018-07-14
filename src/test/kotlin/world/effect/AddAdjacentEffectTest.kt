package world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import world.RoomContent
import world.World
import java.awt.Point

class AddAdjacentEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add breeze to adjacent rooms as effect of pit`() {
        world.addRoomContent(pointToAddTo, RoomContent.PIT)
        for (point in pointToAddTo.adjacents()) {
            assertTrue(world.hasRoomContent(point, RoomContent.BREEZE))
        }
        for (point in pointToAddTo.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }
}