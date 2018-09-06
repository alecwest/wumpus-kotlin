package game.world.effect

import game.world.GameObject
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.World
import org.junit.jupiter.api.Assertions.*
import java.awt.Point
import kotlin.test.assertFalse

class AdjacentEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add breeze to adjacent rooms as effect of pit`() {
        world.addGameObjectAndEffects(point, GameObject.PIT)
        for (point in point.adjacents()) {
            assertTrue(world.hasGameObject(point, GameObject.BREEZE))
        }
        for (point in point.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }

    @Test
    fun `remove breeze from adjacent rooms as result of pit removal`() {
        val world = Helpers.createWorld(gameObject = mapOf(point to setOf(GameObject.PIT)))
        world.removeGameObject(point, GameObject.PIT)
        for (point in point.adjacents() + point.diagonals()) {
            assertFalse(world.hasGameObject(point, GameObject.BREEZE))
        }
    }

    @Test
    fun `get rooms affected`() {
        assertEquals(AdjacentEffect(GameObject.SUPMUW).roomsAffected(Point(2, 2)),
                setOf(Point(1, 2), Point(3, 2), Point(2, 3), Point(2, 1)))
    }

    @Test
    fun `test equality`() {
        assertEquals(AdjacentEffect(GameObject.PIT), AdjacentEffect(GameObject.PIT))
        assertNotEquals(AdjacentEffect(GameObject.PIT), AdjacentEffect(GameObject.BREEZE))
    }
}