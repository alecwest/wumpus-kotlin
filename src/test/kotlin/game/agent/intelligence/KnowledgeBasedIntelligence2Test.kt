package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.agent.intelligence.IntelligenceTest.Companion.world
import game.world.GameObject.*
import game.world.Perception
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

    @Test
    fun `process last move in a breeze room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS, BREEZE))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS_NO, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(2, 2).north(), HAS, PIT))
    }

    @Test
    fun `process last move in a corner room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).south(), HAS, PIT))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).west(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).north(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).east(), HAS, PIT))
    }

    @Test
    fun `assess edge room case`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4))))
        assertEquals(true, intelligence.playerOnEdge())
    }
}