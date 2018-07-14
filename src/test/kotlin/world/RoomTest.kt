package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import world.Util.Companion.createRoom

class RoomTest {
    private val room: Room = createRoom()
    private val initialSize: Int = room.roomContent.size

    @Test
    fun `add content to room`() {
        room.addRoomContent(RoomContent.GLITTER)
        assertEquals(initialSize + 1, room.roomContent.size)
    }

    @Test
    fun `remove content from room`() {
        room.removeRoomContent(RoomContent.BREEZE)
        assertEquals(initialSize - 1, room.roomContent.size)
    }

    @Test
    fun `check room for content`() {
        assertTrue(room.hasRoomContent(RoomContent.BREEZE))
        assertFalse(room.hasRoomContent(RoomContent.GLITTER))
    }

    @Test
    fun `check room is empty`() {
        val emptyRoom = Room(arrayListOf())
        assertTrue(emptyRoom.isEmpty())
    }
}
