package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import world.Util.Companion.createRoom

class RoomTest {
    @Test
    fun `add content to room`() {
        val room: Room = createRoom() // TODO stop initializing in every test
        val initialSize: Int = room.roomContent.size
        room.addRoomContent(RoomContent.GLITTER)
        assertEquals(initialSize + 1, room.roomContent.size)
    }

    @Test
    fun `remove content from room`() {
        val room: Room = createRoom()
        val initialSize: Int = room.roomContent.size
        room.removeRoomContent(RoomContent.BREEZE)
        assertEquals(initialSize - 1, room.roomContent.size)
    }

    @Test
    fun `check room for content`() {
        val room: Room = createRoom()
        assertTrue(room.hasRoomContent(RoomContent.BREEZE))
        assertFalse(room.hasRoomContent(RoomContent.GLITTER))
    }
}
