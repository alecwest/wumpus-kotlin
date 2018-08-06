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
        for (gameObject in gameObjectValues().filter { it.hasCharacteristic(GameObjectCharacteristic.Mappable()) }) {
        }
    }

    @Test
    fun `filter game object values by characteristic`() {
        assertTrue(setOf(GameObject.BREEZE, GameObject.GLITTER,
                GameObject.MOO, GameObject.STENCH,
                GameObject.BLOCKADE, GameObject.FOOD).containsAll(
                        setWithCharacteristics(setOf(GameObjectCharacteristic.Perceptable()))))
    }

    @Test
    fun `has characteristic`() {
        assertTrue(GameObject.STENCH.hasCharacteristic(GameObjectCharacteristic.Perceptable()))
        assertFalse(GameObject.GOLD.hasCharacteristic(GameObjectCharacteristic.Perceptable()))
    }
}

