package game.world

import game.world.GameObject.*
import game.world.GameObjectFeature.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class RoomContentTest {
    @Test
    fun `convert between string and room content`() {
        for (roomContent in roomContentValues()) {
            assertEquals(roomContent, roomContent.toCharRepresentation().toRoomContent())
        }
    }

    @Test
    fun `convert to perception`() {
        assertEquals(Perception.BLOCKADE_BUMP, RoomContent.BLOCKADE.toPerception())
        assertEquals(null, Dangerous1.PIT.toPerception())
    }

    @Test
    fun `convert invalid string`() {
        assertFails { "invalid string".toRoomContent() }
    }



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

        }
    }

    @Test
    fun `has feature`() {
        assertTrue(STENCH.hasFeature(Perceptable()))
        assertFalse(GOLD.hasFeature(Perceptable()))
    }
}

