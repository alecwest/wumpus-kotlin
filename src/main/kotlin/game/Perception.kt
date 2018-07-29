package game

enum class Perception {
    BREEZE,
    BUMP,
    GLITTER,
    MOO,
    STENCH,
    WALL
}

fun Perception.toCharRepresentation() = when(this) {
    Perception.BREEZE -> "="
    Perception.BUMP -> "@"
    Perception.GLITTER -> "*"
    Perception.MOO -> "!"
    Perception.STENCH -> "~"
    Perception.WALL -> "#"
}

fun String.toPerception() = when(this) {
    "=" -> Perception.BREEZE
    "@" -> Perception.BUMP
    "*" -> Perception.GLITTER
    "!" -> Perception.MOO
    "~" -> Perception.STENCH
    "#" -> Perception.WALL
    else -> throw Exception("Cannot convert %s to a Perception value".format(this))
}