package game.agent.intelligence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

import game.agent.intelligence.Fact.*
import game.world.GameObject.*

internal class FactMapTest {
    private val factMap = FactMap()

    @Test
    fun `add fact`() {
        factMap.addFact(Point(2, 3), HAS, SUPMUW)
        assertEquals(mapOf(Point(2, 3) to setOf(Pair(SUPMUW, HAS))), factMap.factMap)
    }
}