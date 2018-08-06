package game.world

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
        for (gameObject in gameObjectValues().filter { it.hasFeature(GameObjectFeature.Mappable()) }) {
        }
    }

    @Test
    fun `filter game object values by feature`() {
        assertTrue(setOf(GameObject.BREEZE, GameObject.GLITTER,
                GameObject.MOO, GameObject.STENCH,
                GameObject.BLOCKADE, GameObject.FOOD).containsAll(
                        gameObjectsWithFeatures(setOf(GameObjectFeature.Perceptable()))))
    }

    @Test
    fun `has feature`() {
        assertTrue(GameObject.STENCH.hasFeature(GameObjectFeature.Perceptable()))
        assertFalse(GameObject.GOLD.hasFeature(GameObjectFeature.Perceptable()))
    }
}

