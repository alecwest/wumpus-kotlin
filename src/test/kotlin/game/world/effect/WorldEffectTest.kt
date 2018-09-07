package game.world.effect

import game.world.GameObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.adjacents
import java.awt.Point
import kotlin.test.assertFalse

internal class WorldEffectTest {
    val world = Helpers.createWorld()
    val point = Point(1, 1)

    // TODO parameterize and test all effects
    @Test
    fun `apply affect`() {
        val effect = AdjacentEffect(GameObject.GOLD)
        Assertions.assertTrue(effect.applyEffect(world, point))
        for (point in point.adjacents()) {
            Assertions.assertTrue(world.hasGameObject(point, GameObject.GOLD))
            assertFalse(world.hasGameObject(point, GameObject.GLITTER))
        }
        Assertions.assertTrue(effect.applyEffect(world, Point(0, 0)))
        assertFalse(effect.applyEffect(Helpers.createWorld(1), Point(0, 0)))
    }
}