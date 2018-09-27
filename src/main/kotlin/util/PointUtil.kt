package util

import java.awt.Point

/**
 * @return [Point] that is adjacent on the northern side
 */
fun Point.north(): Point {
    return Point(this.x, this.y + 1)
}

/**
 * @return [Point] that is adjacent on the eastern side
 */
fun Point.east(): Point {
    return Point(this.x + 1, this.y)
}

/**
 * @return [Point] that is adjacent on the southern side
 */
fun Point.south(): Point {
    return Point(this.x, this.y - 1)
}

/**
 * @return [Point] that is adjacent on the western side
 */
fun Point.west(): Point {
    return Point(this.x - 1, this.y)
}

/**
 * @return [Point] that is diagonal on the north-eastern side
 */
fun Point.northEast(): Point {
    return Point(this.x + 1, this.y + 1)
}

/**
 * @return [Point] that is diagonal on the north-western side
 */
fun Point.northWest(): Point {
    return Point(this.x - 1, this.y + 1)
}

/**
 * @return [Point] that is diagonal on the south-eastern side
 */
fun Point.southEast(): Point {
    return Point(this.x + 1, this.y - 1)
}

/**
 * @return [Point] that is diagonal on the south-western side
 */
fun Point.southWest(): Point {
    return Point(this.x - 1, this.y - 1)
}

/**
 * @param direction direction of room to return
 *
 * @return [Point] of adjacent room on specified side
 */
fun Point.adjacent(direction: Direction): Point {
    return when(direction) {
        Direction.NORTH -> this.north()
        Direction.EAST -> this.east()
        Direction.SOUTH -> this.south()
        Direction.WEST -> this.west()
    }
}

/**
 * @return [Set] of all adjacent rooms
 */
fun Point.adjacents(): Set<Point> {
    return setOf(north(), east(), south(), west())
}

/**
 * @return [Set] of all diagonal rooms
 */
fun Point.diagonals(): Set<Point> {
    return setOf(northEast(), northWest(), southEast(), southWest())
}

/**
 * @param point adjacent room in question
 *
 * @return [Direction] from the passed point to the point calling the function, or null if the points are not adjacent
 */
fun Point.directionFrom(point: Point): Direction? {
    for (direction in Direction.values()) {
        if (point.adjacent(direction) == this)
            return direction
    }
    return null
}

/**
 * @param x
 * @param y
 *
 * @return [Point] deep copy of the point with only passed values modified
 */
fun Point.copyThis(x: Int = this.x, y: Int = this.y) = Point(x, y)
