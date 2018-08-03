package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class RoomContentTest {
    @Test
    fun `convert between string and room content`() {
        for (roomContent in roomContentValues()) {
            assertEquals(roomContent, roomContent.toCharRepresentation().toRoomContent())
        }
    }

    @Test
    fun `convert to perception`() {
        assertEquals(Perception.BLOCKADE_BUMP, RoomContent.BLOCKADE.toPerception())
        assertEquals(null, RoomContent.PIT.toPerception())
    }

    @Test
    fun `convert invalid string`() {
        assertFails { "invalid string".toRoomContent() }
    }
}

