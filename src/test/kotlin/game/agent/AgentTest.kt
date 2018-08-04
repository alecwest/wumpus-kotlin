package game.agent

import game.server.Server
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AgentTest {
    val agent = Agent()

    @Test
    fun `initialize client`() {
        agent.createGame(worldSize = 5)
        assertEquals(5,
                Server.getGame(agent.client.sessionId).getGameState().getWorldSize())
    }
}