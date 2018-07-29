package game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PerceptionTest {
    @Test
    fun `convert between string and perception`() {
        for (perception in Perception.values()) {
            assertEquals(perception, perception.toCharRepresentation().toPerception())
        }
    }
}