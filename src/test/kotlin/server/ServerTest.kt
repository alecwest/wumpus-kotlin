package server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServerTest {
    @Test
    fun `server generates world from world size only`() {
        val worldSize = 10
        val server = Server(worldSize = worldSize)

        assertEquals(worldSize, server.getWorldSize())
        assertEquals(worldSize * worldSize, server.getNumberRooms())
    }

    @Test
    fun `server generates world from json file`() {
        val fileName = "/testFile.json"
        val server = Server(fileName = fileName)

        assertEquals(11, server.getWorldSize())
        assertEquals(121, server.getNumberRooms())
    }
}