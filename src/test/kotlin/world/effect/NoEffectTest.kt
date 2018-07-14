package world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import world.RoomContent
import world.World
import java.awt.Point

class NoEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add no effect to rooms`() {
        world.addRoomContent(pointToAddTo, RoomContent.BLOCKADE)
        for(point in pointToAddTo.adjacents() + pointToAddTo.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }
}