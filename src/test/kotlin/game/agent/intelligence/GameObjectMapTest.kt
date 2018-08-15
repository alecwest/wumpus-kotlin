package game.agent.intelligence

import game.world.GameObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class GameObjectMapTest {
    val objectMap = GameObjectMap(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD),
            Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)))
    val originalMap = GameObjectMap(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD),
            Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)))

    @Test
    fun `add object`() {
        objectMap.add(Point(0, 2), GameObject.MOO)
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD),
                Point(0, 2) to mutableSetOf(GameObject.MOO),
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)), objectMap.getMap())
    }

    @Test
    fun `add map`() {
        objectMap.add(mapOf(Point(1, 2) to setOf(GameObject.GOLD), Point(0, 1) to setOf(GameObject.ARROW)))
        GameObjectMap(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD, GameObject.ARROW),
                Point(1, 2) to mutableSetOf<GameObject>(GameObject.GOLD),
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)))
    }

    @Test
    fun `add existing object`() {
        objectMap.add(Point(0, 1), GameObject.SUPMUW)
        assertEquals(originalMap.getMap(), objectMap.getMap())
    }

    @Test
    fun `add empty point`() {
        objectMap.add(Point(4, 4))
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD),
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW),
                Point(4, 4)to mutableSetOf()), objectMap.getMap())
    }

    @Test
    fun `add empty point where content already exists`() {
        objectMap.add(Point(0, 1))
        assertEquals(originalMap.getMap(), objectMap.getMap())
    }

    @Test
    fun `remove object`() {
        objectMap.remove(Point(0, 1), GameObject.SUPMUW)
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf<GameObject>(GameObject.GOLD),
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)), objectMap.getMap())
    }

    @Test
    fun `remove objects`() {
        objectMap.remove(Point(0, 1))
        assertEquals(mutableMapOf(Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)), objectMap.getMap())
    }

    @Test
    fun `remove only object at point`() {
        objectMap.remove(Point(3, 3), GameObject.ARROW)
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD)), objectMap.getMap())
    }

    @Test
    fun `remove non-existent object`() {
        objectMap.remove(Point(0, 2), GameObject.MOO)
        assertEquals(originalMap.getMap(), objectMap.getMap())
    }

    @Test
    fun `get map`() {
        val map = objectMap.getMap()
        assertEquals(setOf(GameObject.ARROW), map[Point(3, 3)])
        assertEquals(null, map[Point(4, 4)])
    }

    @Test
    fun `get value`() {
        assertEquals(setOf(GameObject.SUPMUW, GameObject.GOLD), objectMap.getValue(Point(0, 1)))
        assertEquals(setOf<GameObject>(), objectMap.getValue(Point(3, 45)))
    }

    @Test
    fun `is null`() {
        assertTrue(objectMap.isNull(Point(4, 9)))
        assertFalse(objectMap.isNull(Point(3, 3)))
    }
}