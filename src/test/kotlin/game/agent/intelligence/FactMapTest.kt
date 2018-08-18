package game.agent.intelligence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.world.GameObject.*

internal class FactMapTest {
    private val factMap = FactMap()

    @Test
    fun `add fact`() {
        factMap.addFact(Point(2, 3), HAS, SUPMUW)
        assertEquals(mapOf(Point(2, 3) to setOf(Pair(SUPMUW, HAS))), factMap.getMap())
    }

    @Test
    fun `update fact that already exists`() {
        factMap.addFact(Point(2, 3), HAS, SUPMUW)
        assertEquals(mapOf(Point(2, 3) to setOf(Pair(SUPMUW, HAS))), factMap.getMap())
        factMap.addFact(Point(2, 3), HAS_NO, SUPMUW)
        assertEquals(mapOf(Point(2, 3) to setOf(Pair(SUPMUW, HAS_NO))), factMap.getMap())
    }

    @Test
    fun `test for truthfulness`() {
        factMap.addFact(Point(2, 3), HAS, SUPMUW)
        assertEquals(TRUE, factMap.isTrue(Point(2, 3), HAS, SUPMUW))
        assertEquals(FALSE, factMap.isTrue(Point(2, 3), HAS_NO, SUPMUW))
        assertEquals(UNKNOWN, factMap.isTrue(Point(2, 5), HAS, SUPMUW))
        assertEquals(UNKNOWN, factMap.isTrue(Point(2, 3), HAS, BREEZE))
    }

    @Test
    fun `test for fact existence`() {
        assertEquals(false, factMap.factExists(Point(2, 3), SUPMUW))
        factMap.addFact(Point(2, 3), HAS, SUPMUW)
        assertEquals(true, factMap.factExists(Point(2, 3), SUPMUW))
    }
}