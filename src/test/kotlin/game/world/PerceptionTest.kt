package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PerceptionTest {
    @Test
    fun `convert perceptions to game objects`() {
        assertEquals(toGameObjects(setOf(Perception.FOOD, Perception.GLITTER)), setOf(GameObject.FOOD, GameObject.GLITTER))
    }
}