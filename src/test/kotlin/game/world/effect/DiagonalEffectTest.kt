package game.world.effect

import game.world.GameObject
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.World
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import java.awt.Point
import kotlin.test.assertFalse

class DiagonalEffectTest {
    private val world: World = World(3)
    private val point: Point = Point(1, 1)

    @Test
    fun `add moo to surrounding rooms as effect of supmuw`() {
        world.addGameObject(point, GameObject.SUPMUW)
        for (point in point.adjacents() + point.diagonals()) {
            assertTrue(world.hasGameObject(point, GameObject.MOO))
        }
    }

    @Test
    fun `remove moo from surrounding rooms as result of supmuw removal`() {
        val world = Helpers.createWorld(gameObject = mapOf(point to arrayListOf(GameObject.SUPMUW)))
        world.removeGameObject(point, GameObject.SUPMUW)
        for (point in point.adjacents() + point.diagonals()) {
            assertFalse(world.hasGameObject(point, GameObject.MOO))
        }
    }

    @Test
    fun `get rooms affected`() {
        assertEquals(DiagonalEffect(GameObject.SUPMUW).roomsAffected(Point(2, 2)),
                setOf(Point(3, 3), Point(1, 1), Point(1, 3), Point(3, 1)))
    }

    @Test
    fun `test equality`() {
        assertEquals(DiagonalEffect(GameObject.BREEZE), DiagonalEffect(GameObject.BREEZE))
        assertNotEquals(DiagonalEffect(GameObject.BREEZE), DiagonalEffect(GameObject.PIT))
    }
}