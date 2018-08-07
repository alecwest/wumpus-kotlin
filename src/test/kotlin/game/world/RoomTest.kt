package game.world

import game.world.GameObjectFeature.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Helpers.Companion.createRoom

class RoomTest {
    private val room: Room = createRoom()
    private val initialSize: Int = room.getAmountOfObjects()

    @Test
    fun `get room content`() {
        assertEquals(arrayListOf(GameObject.BREEZE, GameObject.STENCH), room.getGameObject())
    }

    @Test
    fun `add content to room`() {
        room.addGameObject(GameObject.GLITTER)
        assertEquals(initialSize + 1, room.getAmountOfObjects())
    }

    @Test
    fun `remove content from room`() {
        room.removeGameObject(GameObject.BREEZE)
        assertEquals(initialSize - 1, room.getAmountOfObjects())
    }

    @Test
    fun `check room for content`() {
        assertTrue(room.hasGameObject(GameObject.BREEZE))
        assertFalse(room.hasGameObject(GameObject.GLITTER))
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
        assertTrue(smallRoomString.contains((GameObject.BREEZE.getFeature(Mappable()) as Mappable).character))
        assertTrue(smallRoomString.contains((GameObject.STENCH.getFeature(Mappable()) as Mappable).character))
    }
}
