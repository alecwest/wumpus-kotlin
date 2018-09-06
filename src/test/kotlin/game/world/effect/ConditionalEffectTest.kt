package game.world.effect

import facts.Fact.*
import game.world.GameObject
import game.world.condition.ProximityCondition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class ConditionalEffectTest {
    val world = Helpers.createWorld()
    val conditionalEffect = ConditionalEffect(HereEffect(GameObject.BREEZE), ProximityCondition(HAS, GameObject.GOLD))

    @Test
    fun `test ConditionalEffect equality`() {
        assertEquals(ConditionalEffect(HereEffect(GameObject.FOOD), ProximityCondition(HAS, GameObject.SUPMUW)),
                ConditionalEffect(HereEffect(GameObject.FOOD), ProximityCondition(HAS, GameObject.SUPMUW)))
    }

    @Test
    fun `apply conditional effect`() {
        conditionalEffect.applyEffect(world, Point(0, 4))
        assertFalse(world.hasGameObject(Point(0, 4), GameObject.BREEZE))

        world.addGameObject(Point(0, 3), GameObject.GOLD)
        conditionalEffect.applyEffect(world, Point(0, 4))
        assertTrue(world.hasGameObject(Point(0, 4), GameObject.BREEZE))
    }

    @Test
    fun `remove conditional effect`() {
        world.addGameObject(Point(0, 4), GameObject.BREEZE)
        conditionalEffect.removeEffect(world, Point(0, 4))
        assertFalse(world.hasGameObject(Point(0, 4), GameObject.BREEZE))
    }

    @Test
    fun `check rooms affected`() {
        assertEquals(conditionalEffect.roomsAffected(Point(0, 4)), HereEffect(GameObject.BREEZE).roomsAffected(Point(0, 4)))
    }
}