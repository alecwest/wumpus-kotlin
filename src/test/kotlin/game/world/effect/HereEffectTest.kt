package game.world.effect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import game.world.GameObject
import game.world.World
import java.awt.Point
import kotlin.test.assertFalse

class HereEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add glitter to current room as result of gold`() {
        world.addGameObject(point, GameObject.GOLD)
        assertTrue(world.hasGameObject(point, GameObject.GLITTER))
    }

    @Test
    fun `remove glitter from current room as result of gold removal`() {
        val world = Helpers.createWorld(gameObject = mapOf(point to arrayListOf(GameObject.GOLD)))
        world.removeGameObject(point, GameObject.GOLD)
        assertFalse(world.hasGameObject(point, GameObject.GLITTER))
    }
}