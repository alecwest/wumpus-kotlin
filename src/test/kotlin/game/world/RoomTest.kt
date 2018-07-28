package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Helpers.Companion.createRoom

class RoomTest {
    private val room: Room = createRoom()
    private val initialSize: Int = room.getAmountOfContent()

    @Test
    fun `get room content`() {
        assertEquals(arrayListOf(RoomContent.BREEZE, RoomContent.STENCH), room.getRoomContent())
    }

    @Test
    fun `add content to room`() {
        room.addRoomContent(RoomContent.GLITTER)
        assertEquals(initialSize + 1, room.getAmountOfContent())
    }

    @Test
    fun `remove content from room`() {
        room.removeRoomContent(RoomContent.BREEZE)
        assertEquals(initialSize - 1, room.getAmountOfContent())
    }

    @Test
    fun `check room for content`() {
        assertTrue(room.hasRoomContent(RoomContent.BREEZE))
        assertFalse(room.hasRoomContent(RoomContent.GLITTER))
    }

    @Test
    fun `check room is empty`() {
        val emptyRoom = Room()
        assertTrue(emptyRoom.isEmpty())
    }

    @Test
    fun `print small room`() {
        val smallRoomString = room.getSmallRoomString()
        assertFalse(smallRoomString.contains("x"))
        assertTrue(smallRoomString.contains(RoomContent.BREEZE.toCharRepresentation()))
        assertTrue(smallRoomString.contains(RoomContent.STENCH.toCharRepresentation()))
    }
}
