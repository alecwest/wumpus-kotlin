package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Point

class PointUtilTest {
    @Test
    fun `get adjacent points`() {
        val point = Point(3,5)
        val adjacentPoints = point.adjacents()
        assertEquals(4, adjacentPoints.size)
        assertEquals(Point(4, 5), adjacentPoints[0])
        assertEquals(Point(3, 6), adjacentPoints[1])
        assertEquals(Point(2, 5), adjacentPoints[2])
        assertEquals(Point(3, 4), adjacentPoints[3])
    }

    @Test
    fun `get diagonal points`() {
        val point = Point(3,5)
        val diagonalPoints = point.diagonals()
        assertEquals(4, diagonalPoints.size)
        assertEquals(Point(4, 6), diagonalPoints[0])
        assertEquals(Point(4, 4), diagonalPoints[1])
        assertEquals(Point(2, 6), diagonalPoints[2])
        assertEquals(Point(2, 4), diagonalPoints[3])
    }
}