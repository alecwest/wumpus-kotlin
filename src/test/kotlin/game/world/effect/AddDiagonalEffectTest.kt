package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.RoomContent
import game.world.World
import java.awt.Point

class AddDiagonalEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add moo to surrounding rooms as effect of supmuw`() {
        world.addRoomContent(pointToAddTo, RoomContent.SUPMUW)
        for (point in pointToAddTo.adjacents() + pointToAddTo.diagonals()) {
            assertTrue(world.hasRoomContent(point, RoomContent.MOO))
        }
    }
}