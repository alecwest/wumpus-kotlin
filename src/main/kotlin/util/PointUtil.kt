package util

import java.awt.Point

fun Point.north(): Point {
    return Point(this.x, this.y + 1)
}

fun Point.east(): Point {
    return Point(this.x + 1, this.y)
}

fun Point.south(): Point {
    return Point(this.x, this.y - 1)
}

fun Point.west(): Point {
    return Point(this.x - 1, this.y)
}

fun Point.northEast(): Point {
    return Point(this.x + 1, this.y + 1)
}

fun Point.northWest(): Point {
    return Point(this.x - 1, this.y + 1)
}

fun Point.southEast(): Point {
    return Point(this.x + 1, this.y - 1)
}

fun Point.southWest(): Point {
    return Point(this.x - 1, this.y - 1)
}

fun Point.adjacent(direction: Direction): Point {
    return when(direction) {
        Direction.NORTH -> this.north()
        Direction.EAST -> this.east()
        Direction.SOUTH -> this.south()
        Direction.WEST -> this.west()
    }
}

fun Point.adjacents(): Set<Point> {
    return setOf(north(), east(), south(), west())
}

fun Point.diagonals(): Set<Point> {
    return setOf(northEast(), northWest(), southEast(), southWest())
}

fun Point.directionFrom(point: Point): Direction? {
    for (direction in Direction.values()) {
        if (point.adjacent(direction) == this)
            return direction
    }
    return null
}

fun Point.copyThis(x: Int = this.x, y: Int = this.y) = Point(x, y)
