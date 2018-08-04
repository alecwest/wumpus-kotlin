package game.agent

import game.client.Client
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

    }
}