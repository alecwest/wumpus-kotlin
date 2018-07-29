package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RoomContentTest {
    @Test
    fun `convert between string and room content`() {
        for (roomContent in RoomContent.values()) {
            assertEquals(roomContent, roomContent.toCharRepresentation().toRoomContent())
        }
    }
}