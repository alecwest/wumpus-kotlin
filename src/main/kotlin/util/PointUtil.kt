package util

import java.awt.Point

fun Point.north(): Point {
    return Point(this.x + 1, this.y)
}

fun Point.east(): Point {
    return Point(this.x, this.y + 1)
}

fun Point.south(): Point {
    return Point(this.x - 1, this.y)
}

fun Point.west(): Point {
    return Point(this.x, this.y - 1)
}

fun Point.northEast(): Point {
    return Point(this.x + 1, this.y + 1)
}

fun Point.northWest(): Point {
    return Point(this.x + 1, this.y - 1)
}

fun Point.southEast(): Point {
    return Point(this.x - 1, this.y + 1)
}

fun Point.southWest(): Point {
    return Point(this.x - 1, this.y - 1)
}

fun Point.adjacents(): ArrayList<Point> {
    return arrayListOf(north(), east(), south(), west())
}

fun Point.diagonals(): ArrayList<Point> {
    return arrayListOf(northEast(), northWest(), southEast(), southWest())
}

fun Point.copyThis(x: Int = this.x, y: Int = this.y) = Point(x, y)
