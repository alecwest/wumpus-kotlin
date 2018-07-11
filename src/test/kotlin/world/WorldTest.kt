package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import world.Util.Companion.createRoom

// TODO use @ParameterizedTest and @MethodSource
class WorldTest {
    private val world: World = World(arrayListOf(createRoom(arrayListOf()), createRoom(),
                createRoom(arrayListOf()), createRoom()))

    @Test
    fun `add content to room`() {
        val initialNumberItems = world.rooms[world.getRoomIndex(1, 1)].roomContent.size
        world.addRoomContent(1, 1, RoomContent.FOOD)
        assertEquals(initialNumberItems + 1, world.rooms[world.getRoomIndex(1, 1)].roomContent.size)
    }

    @Test
    fun `remove content from room`() {
        val initialNumberItems = world.rooms[world.getRoomIndex(1, 1)].roomContent.size
        world.removeRoomContent(1, 1, RoomContent.BREEZE)
        assertEquals(initialNumberItems - 1, world.rooms[world.getRoomIndex(1, 1)].roomContent.size)
    }

    @Test
    fun `check room for content`() {
        assertTrue(world.hasRoomContent(1, 1, RoomContent.BREEZE))
        assertFalse(world.hasRoomContent(1, 1, RoomContent.FOOD))
    }

    @Test
    fun `get index of room`() {
        assertEquals(0, world.getRoomIndex(0, 0))
        assertEquals(1, world.getRoomIndex(1, 0))
        assertEquals(2, world.getRoomIndex(0, 1))
        assertEquals(3, world.getRoomIndex(1, 1))
    }

    @Test
    fun `increase dimensions of world`() {
        val newDimension = 4
        world.setWorldDimension(newDimension)
        assertEquals(newDimension, world.getWorldDimension())
    }

    @Test
    fun `decrease dimensions of world`() {
        val newDimension = 1
        world.setWorldDimension(newDimension)
        assertEquals(newDimension, world.getWorldDimension())
    }

    @Test
    fun `get dimensions of world`() {
        assertEquals(2, world.getWorldDimension())
    }
}
