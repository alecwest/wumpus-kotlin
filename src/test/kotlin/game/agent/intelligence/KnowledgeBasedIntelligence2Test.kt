package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.agent.intelligence.IntelligenceTest.Companion.world
import game.world.GameObject.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.*
import java.awt.Point

internal class KnowledgeBasedIntelligence2Test {
    val intelligence = KnowledgeBasedIntelligence2()

    @Test
    fun `process last move in empty room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS_NO, FOOD))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(2, 2), HAS, ARROW))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2).north(), HAS_NO, PIT))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2).southEast(), HAS_NO, SUPMUW))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(2, 2).southEast(), HAS_NO, GLITTER))
    }
}