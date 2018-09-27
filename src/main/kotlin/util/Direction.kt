package util

/**
 * Enumeration of the four cardinal directions
 */
enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
}

/**
 * @return [Direction] value 90 degrees left
 */
fun Direction.left() = when(this) {
    Direction.NORTH -> Direction.WEST
    Direction.EAST -> Direction.NORTH
    Direction.SOUTH -> Direction.EAST
    Direction.WEST -> Direction.SOUTH
}

/**
 * @return [Direction] value 90 degrees right
 */
fun Direction.right() = when(this) {
    Direction.NORTH -> Direction.EAST
    Direction.EAST -> Direction.SOUTH
    Direction.SOUTH -> Direction.WEST
    Direction.WEST -> Direction.NORTH
}

/**
 * @return [String] character for direction
 */
fun Direction.toCharRepresentation() = when(this) {
    Direction.NORTH -> "N"
    Direction.EAST -> "E"
    Direction.SOUTH -> "S"
    Direction.WEST -> "W"
}

/**
 * @return [Direction] represented by the String
 */
fun String.toDirection() = when(this) {
    Direction.NORTH.toCharRepresentation() -> Direction.NORTH
    Direction.NORTH.toPlayerMapRepresentation() -> Direction.NORTH
    Direction.EAST.toCharRepresentation() -> Direction.EAST
    Direction.EAST.toPlayerMapRepresentation() -> Direction.EAST
    Direction.SOUTH.toCharRepresentation() -> Direction.SOUTH
    Direction.SOUTH.toPlayerMapRepresentation() -> Direction.SOUTH
    Direction.WEST.toCharRepresentation() -> Direction.WEST
    Direction.WEST.toPlayerMapRepresentation() -> Direction.WEST

    else -> throw Exception("Cannot convert %s to a Direction value".format(this))
}

/**
 * @return [String] character for player direction
 */
fun Direction.toPlayerMapRepresentation() = when(this) {
    Direction.NORTH -> "^"
    Direction.EAST -> ">"
    Direction.SOUTH -> "v"
    Direction.WEST -> "<"
}