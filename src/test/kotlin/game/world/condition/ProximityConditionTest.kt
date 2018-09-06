package game.world.condition

import facts.Fact.*
import game.world.GameObject.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class ProximityConditionTest {
    val world = Helpers.createWorld()

    @Test
    fun `check condition satisfied when expecting an object`() {
        world.addGameObjectAndEffects(Point(0, 2), PIT)
        assertTrue(ProximityCondition(HAS, PIT).conditionSatisfied(Point(0, 1), world))
    }

    @Test
    fun `check condition satisfied when expecting no object`() {
        assertTrue(ProximityCondition(HAS_NO, PIT).conditionSatisfied(Point(0, 1), world))
    }

    @Test
    fun `check condition not satisfied when expecting an object`() {
        assertFalse(ProximityCondition(HAS, PIT).conditionSatisfied(Point(0, 1), world))
    }

    @Test
    fun `check condition not satisfied when expecting no object`() {
        world.addGameObjectAndEffects(Point(0, 2), PIT)
        assertFalse(ProximityCondition(HAS_NO, PIT).conditionSatisfied(Point(0, 1), world))
    }
}