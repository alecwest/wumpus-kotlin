package game.world

import game.world.GameObject.*
import game.world.GameObjectFeature.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameObjectTest {
    @Test
    fun `convert mappables between string and game object`() {
        for (gameObject in gameObjectValues().filter { it.hasFeature(Mappable()) }) {
        }
    }

    @Test
    fun `filter game object values by feature`() {
        assertTrue(setOf(BREEZE, GLITTER, MOO, STENCH, BLOCKADE, FOOD).containsAll(
                        gameObjectsWithFeatures(setOf(Perceptable()))))
    }

    @Test
    fun `filter game object values by features that exist together`() {
        assertTrue(setOf(SUPMUW, SUPMUW_EVIL, WUMPUS).containsAll(
                gameObjectsWithFeatures(setOf(Dangerous(), Destructable()))))
    }

    @Test
    fun `filter game object values by features that do not exist together`() {
        assertTrue(gameObjectsWithFeatures(setOf(Shootable(), Destructable())).isEmpty())
    }

    @Test
    fun `get feature from game object`() {
        assertEquals("X", (BLOCKADE.getFeature(Mappable()) as Mappable).character)
    }

    @Test
    fun `convert mappable game objects between string and game object`() {
        for (gameObject in gameObjectsWithFeatures(setOf(Mappable()))) {
            val mappableFeature = gameObject.getFeature(Mappable()) as Mappable
            assertEquals(gameObject, mappableFeature.character.toMappableGameObject())
        }
    }

    @Test
    fun `convert game object to perceptable`() {
        assertEquals(Perception.BLOCKADE_BUMP, (GameObject.BLOCKADE.getFeature(Perceptable()) as Perceptable).perception)
        assertEquals(Perception.BREEZE, (GameObject.BREEZE.getFeature(Perceptable()) as Perceptable).perception)
        assertEquals(Perception.FOOD, (GameObject.FOOD.getFeature(Perceptable()) as Perceptable).perception)
        assertEquals(Perception.GLITTER, (GameObject.GLITTER.getFeature(Perceptable()) as Perceptable).perception)
        assertEquals(Perception.MOO, (GameObject.MOO.getFeature(Perceptable()) as Perceptable).perception)
        assertEquals(Perception.STENCH, (GameObject.STENCH.getFeature(Perceptable()) as Perceptable).perception)
    }

    @Test
    fun `has feature`() {
        assertTrue(STENCH.hasFeature(Perceptable()))
        assertFalse(GOLD.hasFeature(Perceptable()))
    }

    @Test
    fun `convert perception to game object`() {
        assertEquals(GameObject.BREEZE, Perception.BREEZE.toGameObject())
        assertEquals(GameObject.FOOD, Perception.FOOD.toGameObject())
        assertEquals(GameObject.GLITTER, Perception.GLITTER.toGameObject())
        assertEquals(GameObject.MOO, Perception.MOO.toGameObject())
        assertEquals(GameObject.STENCH, Perception.STENCH.toGameObject())
    }
}

