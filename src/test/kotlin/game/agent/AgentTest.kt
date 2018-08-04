package game.agent

import game.agent.intelligence.BasicIntelligence
import game.client.Client
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AgentTest {
    private val client = Client(worldSize = 5)
    private val agent = Agent(client, BasicIntelligence())

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