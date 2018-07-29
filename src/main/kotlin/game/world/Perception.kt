package game.world

enum class Perception {
    BREEZE,
    BUMP,
    GLITTER,
    MOO,
    STENCH,
    WALL
}

fun Perception.toCharRepresentation() = when(this) {
    Perception.BREEZE -> RoomContent.BREEZE.toCharRepresentation()
    Perception.BUMP -> RoomContent.BUMP.toCharRepresentation()
    Perception.GLITTER -> RoomContent.GLITTER.toCharRepresentation()
    Perception.MOO -> RoomContent.MOO.toCharRepresentation()
    Perception.STENCH -> RoomContent.STENCH.toCharRepresentation()
    Perception.WALL -> RoomContent.WALL.toCharRepresentation()
}

fun String.toPerception() = when(this) {
    RoomContent.BREEZE.toCharRepresentation() -> Perception.BREEZE
    RoomContent.BUMP.toCharRepresentation() -> Perception.BUMP
    RoomContent.GLITTER.toCharRepresentation() -> Perception.GLITTER
    RoomContent.MOO.toCharRepresentation() -> Perception.MOO
    RoomContent.STENCH.toCharRepresentation() -> Perception.STENCH
    RoomContent.WALL.toCharRepresentation() -> Perception.WALL
    else -> throw Exception("Cannot convert %s to a Perception value".format(this))
}