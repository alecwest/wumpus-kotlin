package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.RoomContent
import game.world.World
import game.world.WorldState
import java.awt.Point

class NoEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add nothing to rooms as effect of blockade`() {
        world.addRoomContent(pointToAddTo, RoomContent.BLOCKADE)
        for(point in pointToAddTo.adjacents() + pointToAddTo.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }
}