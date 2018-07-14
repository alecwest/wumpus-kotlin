package world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import world.RoomContent
import world.World
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