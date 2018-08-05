package util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DirectionTest {
    @Test
    fun `check left`() {
        assertEquals(Direction.WEST, Direction.NORTH.left())
        assertEquals(Direction.SOUTH, Direction.WEST.left())
    }

    @Test
    fun `check right`() {
        assertEquals(Direction.WEST, Direction.SOUTH.right())
        assertEquals(Direction.SOUTH, Direction.EAST.right())
    }

    @Test
    fun `convert between char and direction`() {
        for (direction in Direction.values()) {
            assertEquals(direction, direction.toCharRepresentation().toDirection())
        }
    }

    @Test
    fun `convert between player map representation and direction`() {
        for (direction in Direction.values()) {
            assertEquals(direction, direction.toPlayerMapRepresentation().toDirection())
        }
    }

    @Test
    fun `convert bad string to direction`() {
        assertFails { "bad string".toDirection() }
    }
}