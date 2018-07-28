package util

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
}

fun Direction.left() = when(this) {
    Direction.NORTH -> Direction.WEST
    Direction.EAST -> Direction.NORTH
    Direction.SOUTH -> Direction.EAST
    Direction.WEST -> Direction.SOUTH
}

fun Direction.right() = when(this) {
    Direction.NORTH -> Direction.EAST
    Direction.EAST -> Direction.SOUTH
    Direction.SOUTH -> Direction.WEST
    Direction.WEST -> Direction.NORTH
}

fun Direction.toCharRepresentation() = when(this) {
    Direction.NORTH -> "N"
    Direction.EAST -> "E"
    Direction.SOUTH -> "S"
    Direction.WEST -> "W"
}

fun String.toDirection() = when(this) {
    "N" -> Direction.NORTH
    "E" -> Direction.EAST
    "S" -> Direction.SOUTH
    "W" -> Direction.WEST
    else -> throw Exception("Cannot convert %s to a Direction value".format(this))
}