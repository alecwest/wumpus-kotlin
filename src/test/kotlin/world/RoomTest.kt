package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RoomTest {
    private var room: Room
    private val initialSize: Int

    init {
        var roomContent: ArrayList<RoomContent> = ArrayList()
        roomContent.add(RoomContent.BREEZE)
        roomContent.add(RoomContent.STENCH)
        room = Room(roomContent)
        initialSize = room.roomContent.size
    }

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
}