package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import game.world.RoomContent
import game.world.World
import game.world.WorldState
import java.awt.Point

class AddHereEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add glitter to current room as result of gold`() {
        world.addRoomContent(pointToAddTo, RoomContent.GOLD)
        assertTrue(world.hasRoomContent(pointToAddTo, RoomContent.GLITTER))
    }
}