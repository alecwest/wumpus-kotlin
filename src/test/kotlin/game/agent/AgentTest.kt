package game.agent

import game.client.Client
import game.server.Server
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AgentTest {
    val client = Client(worldSize = 5)
    val agent = Agent(client)

    @Test
    fun `initialize client`() {
        assertEquals(5, agent.client.getWorldSize())
    }

    @Test
    fun `initialize agent world`() {
        assertEquals(5, agent.world.getSize())
        assertEquals(0, agent.world.getRooms().filter { !it.isEmpty() }.size)
    }
}