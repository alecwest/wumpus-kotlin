package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

// TODO use @ParameterizedTest and @MethodSource
class WorldTest {
    private val size: Int = 2
    private val world: World = World(size)

    init {
        world.addRoomContent(Point(size - 1, size - 1), RoomContent.STENCH)
        world.addRoomContent(Point(size - 1, size - 1), RoomContent.BREEZE)
    }

    @Test
    fun `verify world init`() {
        assertEquals(size * size, world.rooms.size)
    }

    @Test
    fun `add content to room`() {
        val initialNumberItems = world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size
        world.addRoomContent(Point(1, 1), RoomContent.FOOD)
        assertEquals(initialNumberItems + 1, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
    }

    @Test
    fun `remove content from room`() {
        val initialNumberItems = world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size
        world.removeRoomContent(Point(1, 1), RoomContent.BREEZE)
        assertEquals(initialNumberItems - 1, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
    }

    @Test
    fun `check room for content`() {
        assertTrue(world.hasRoomContent(Point(1, 1), RoomContent.BREEZE))
        assertFalse(world.hasRoomContent(Point(1, 1), RoomContent.FOOD))
    }

    @Test
    fun `get index of room`() {
        assertEquals(0, world.getRoomIndex(Point(0, 0)))
        assertEquals(1, world.getRoomIndex(Point(1, 0)))
        assertEquals(2, world.getRoomIndex(Point(0, 1)))
        assertEquals(3, world.getRoomIndex(Point(1, 1)))
        assertEquals(-1, world.getRoomIndex(Point(2, 1)))
    }
}
