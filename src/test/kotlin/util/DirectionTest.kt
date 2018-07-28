package util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
}