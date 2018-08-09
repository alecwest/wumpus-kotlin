package game.agent.intelligence

import game.agent.Agent
import game.client.Client
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class KnowledgeBasedIntelligenceTest {
    val agent = Agent(Client(Helpers.testFilePath + "knowledgeBasedIntelligenceTestFile.json"), KnowledgeBasedIntelligence())

    @Test
    fun `process last move with knowns and possibles`() {

    }
}