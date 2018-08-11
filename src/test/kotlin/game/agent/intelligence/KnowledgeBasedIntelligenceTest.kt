package game.agent.intelligence

import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.CommandResult
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
        assertEquals(mapOf(Point(0, 0) to setOf(GameObject.PIT),
                Point(0, 2) to setOf(GameObject.PIT),
                Point(1, 1) to setOf(GameObject.PIT)), intelligence.possibles)
        assertEquals(mapOf(Point(0, 1) to setOf(GameObject.BREEZE)), intelligence.knowns)
    }

    @Test
    fun `process move without marking visited rooms as possible for content`() {
        val lastMove = Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(0, 0)))
        val lastMove2 = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)),
                arrayListOf(GameObject.BREEZE))
        intelligence.processLastMove(world, lastMove)
        intelligence.processLastMove(world, lastMove2)
        assertEquals(mapOf(Point(0, 2) to setOf(GameObject.PIT),
                Point(1, 1) to setOf(GameObject.PIT)), intelligence.possibles)
        assertEquals(mapOf<Point, Set<GameObject>>(Point(0, 0) to setOf(),
                Point(0, 1) to setOf(GameObject.BREEZE)), intelligence.knowns)
    }
}