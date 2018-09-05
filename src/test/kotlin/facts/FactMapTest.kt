package facts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

import facts.Answer.*
import facts.Fact.*
import game.world.GameObject
import game.world.GameObject.*
import game.world.GameObjectFeature
import game.world.gameObjectValues
import game.world.gameObjectsWithFeatures

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

    @Test
    fun `test room is safe`() {
        assertEquals(FALSE, factMap.roomIsSafe(Point(2, 2)))
        factMap.addFact(Point(2, 2), HAS, SUPMUW)
        assertEquals(FALSE, factMap.roomIsSafe(Point(2, 2)))
        gameObjectsWithFeatures(setOf(GameObjectFeature.Dangerous())).forEach {
            factMap.addFact(Point(4, 4), HAS_NO, it)
        }
        assertEquals(TRUE, factMap.roomIsSafe(Point(4, 4)))
        factMap.addFact(Point(5, 5), HAS, PIT)
        assertEquals(FALSE, factMap.roomIsSafe(Point(5, 5)))
    }

    @Test
    fun `test room can be entered`() {
        factMap.addFact(Point(2, 2), HAS, BLOCKADE)
        assertEquals(FALSE, factMap.canEnterRoom(Point(2, 2)))
        factMap.addFact(Point(0, 5), HAS, WALL)
        assertEquals(FALSE, factMap.canEnterRoom(Point(0, 5)))
        factMap.addFact(Point(0, 3), HAS_NO, BLOCKADE)
        assertEquals(UNKNOWN, factMap.canEnterRoom(Point(0, 3)))
        gameObjectsWithFeatures(setOf(GameObjectFeature.Blocking())).forEach {
            factMap.addFact(Point(4, 4), HAS_NO, it)
        }
        assertEquals(TRUE, factMap.canEnterRoom(Point(4, 4)))
    }


    @Test
    fun `test everything about room is known`() {
        for (gameObject in gameObjectValues()) {
            factMap.addFact(Point(2, 2), HAS_NO, gameObject)
        }
        factMap.addFact(Point(2, 4), HAS_NO, PIT)
        assertTrue(factMap.everythingKnownAboutRoom(Point(2, 2)))
    }

    @Test
    fun `test everything perceptable about room is known`() {
        for (gameObject in
        gameObjectsWithFeatures(setOf(GameObjectFeature.Perceptable()))) {
            factMap.addFact(Point(2, 2), HAS_NO, gameObject)
        }
        factMap.addFact(Point(2, 4), HAS_NO, PIT)
        assertTrue(factMap.featureFullyKnownInRoom(Point(2, 2), GameObjectFeature.Perceptable()))
    }

    @Test
    fun `get effects in room`() {
        factMap.addFact(Point(3, 3), HAS, PIT)
        factMap.addFact(Point(3, 3), HAS, BREEZE)
        factMap.addFact(Point(3, 3), HAS, GLITTER)
        factMap.addFact(Point(5, 5), HAS, SUPMUW)
        assertEquals(setOf(GameObject.BREEZE, GameObject.GLITTER), factMap.getEffectsInRoom(Point(3, 3)))
        assertEquals(setOf<GameObject>(), factMap.getEffectsInRoom(Point(5, 5)))
    }

    @Test
    fun `get all rooms with object`() {
        factMap.addFact(Point(3, 3), HAS, EXIT)
        factMap.addFact(Point(3, 2), HAS_NO, EXIT)
        factMap.addFact(Point(3, 1), HAS, EXIT)
        assertEquals(setOf(Point(3, 3), Point(3, 1)), factMap.roomsWithObject(EXIT))
    }
}