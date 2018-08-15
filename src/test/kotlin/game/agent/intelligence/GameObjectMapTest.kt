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
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)), objectMap.gameObjectMap)
    }

    @Test
    fun `add existing object`() {
        objectMap.add(Point(0, 1), GameObject.SUPMUW)
        assertEquals(originalMap.gameObjectMap, objectMap.gameObjectMap)
    }

    @Test
    fun `remove object`() {
        objectMap.remove(Point(0, 1), GameObject.SUPMUW)
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf<GameObject>(GameObject.GOLD),
                Point(3, 3) to mutableSetOf<GameObject>(GameObject.ARROW)), objectMap.gameObjectMap)
    }

    @Test
    fun `remove only object at point`() {
        objectMap.remove(Point(3, 3), GameObject.ARROW)
        assertEquals(mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.GOLD)), objectMap.gameObjectMap)
    }

    @Test
    fun `remove non-existent object`() {
        objectMap.remove(Point(0, 2), GameObject.MOO)
        assertEquals(originalMap.gameObjectMap, objectMap.gameObjectMap)
    }
}