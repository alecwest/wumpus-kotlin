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
    fun `server generates world from file name`() {
        val fileName = "/testFile.txt"
        val server = Server(fileName = fileName)
        val worldSize = this.javaClass.getResource(fileName)
                .readText().split("\n")[0].toInt()

        assertEquals(worldSize, server.getWorldSize())
        assertEquals(worldSize * worldSize, server.getNumberRooms())
    }
}