package game.agent.intelligence

import game.agent.Agent
import game.agent.intelligence.IntelligenceTest.Companion.world
import game.client.Client
import game.world.GameObject
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class KnowledgeBasedIntelligenceTest {
    val intelligence = KnowledgeBasedIntelligence()

    @Test
    fun `process last move with knowns and possibles`() {
        val lastMove = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)),
                arrayListOf(GameObject.BREEZE))
        intelligence.processLastMove(world, lastMove)
        assertEquals(mapOf(Point(0, 2) to setOf(GameObject.PIT), Point(1, 1) to setOf(GameObject.PIT)), intelligence.possibles)
        assertEquals(emptyMap<Point, Set<GameObject>>(), intelligence.knowns)
    }
}