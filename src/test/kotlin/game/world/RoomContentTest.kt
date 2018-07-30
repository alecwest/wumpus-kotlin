package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class RoomContentTest {
    @Test
    fun `convert between string and room content`() {
        for (roomContent in RoomContent.values()) {
            assertEquals(roomContent, roomContent.toCharRepresentation().toRoomContent())
        }
    }

    @Test
    fun `convert to perception`() {
        assertEquals(Perception.BLOCKADE_BUMP, RoomContent.BLOCKADE.toPerception())
        assertEquals(Perception.BREEZE, RoomContent.BREEZE.toPerception())
        assertEquals(Perception.GLITTER, RoomContent.GLITTER.toPerception())
        assertEquals(Perception.MOO, RoomContent.MOO.toPerception())
        assertEquals(Perception.STENCH, RoomContent.STENCH.toPerception())
        assertEquals(null, RoomContent.PIT.toPerception())
    }

    @Test
    fun `convert invalid string`() {
        assertFails { "invalid string".toRoomContent() }
    }
}

