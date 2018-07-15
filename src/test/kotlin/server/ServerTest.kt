package server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServerTest {
    @Test
    fun `world is generated procedurally`() {
        val worldSize = 10
        val server = Server(worldSize = worldSize)

        assertEquals(worldSize, server.getWorldSize())
        assertEquals(worldSize * worldSize, server.getNumberRooms())
    }
}