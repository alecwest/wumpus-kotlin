package game.world

/**
 * Changes:
 *      All AGENT types are removed in favor of a data structure containing the client's position, direction faced,
 *      and inventory which will be used to render the agent appropriately on the map.
 */
enum class RoomContent {
    ARROW,
    BLOCKADE,
    BREEZE,
    BUMP,
    FOOD,
    GLITTER,
    GOLD,
    MOO,
    PIT,
    STENCH,
    SUPMUW_EVIL,
    SUPMUW,
    WALL,
    WUMPUS
}

fun RoomContent.toPerception(): Perception? {
    return when(this) {
        RoomContent.BLOCKADE -> Perception.BUMP
        RoomContent.BREEZE -> Perception.BREEZE
        RoomContent.BUMP -> Perception.WALL
        RoomContent.GLITTER -> Perception.GLITTER
        RoomContent.MOO -> Perception.MOO
        RoomContent.STENCH -> Perception.STENCH
        else -> null
    }
}

fun RoomContent.toCharRepresentation(): String {
    return when(this){
        RoomContent.ARROW -> "A"
        RoomContent.BLOCKADE -> "X"
        RoomContent.BREEZE -> "="
        RoomContent.BUMP -> "@"
        RoomContent.FOOD -> "F"
        RoomContent.GLITTER -> "*"
        RoomContent.GOLD -> "G"
        RoomContent.MOO -> "!"
        RoomContent.PIT -> "O"
        RoomContent.STENCH -> "~"
        RoomContent.SUPMUW_EVIL -> "E"
        RoomContent.SUPMUW -> "S"
        RoomContent.WALL -> "#"
        RoomContent.WUMPUS -> "W"
    }
}

fun String.toRoomContent(): RoomContent {
    return when(this) {
        RoomContent.ARROW.toCharRepresentation() -> RoomContent.ARROW
        RoomContent.BLOCKADE.toCharRepresentation() -> RoomContent.BLOCKADE
        RoomContent.BREEZE.toCharRepresentation() -> RoomContent.BREEZE
        RoomContent.BUMP.toCharRepresentation() -> RoomContent.BUMP
        RoomContent.FOOD.toCharRepresentation() -> RoomContent.FOOD
        RoomContent.GLITTER.toCharRepresentation() -> RoomContent.GLITTER
        RoomContent.GOLD.toCharRepresentation() -> RoomContent.GOLD
        RoomContent.MOO.toCharRepresentation() -> RoomContent.MOO
        RoomContent.PIT.toCharRepresentation() -> RoomContent.PIT
        RoomContent.STENCH.toCharRepresentation() -> RoomContent.STENCH
        RoomContent.SUPMUW_EVIL.toCharRepresentation() -> RoomContent.SUPMUW_EVIL
        RoomContent.SUPMUW.toCharRepresentation() -> RoomContent.SUPMUW
        RoomContent.WALL.toCharRepresentation() -> RoomContent.WALL
        RoomContent.WUMPUS.toCharRepresentation() -> RoomContent.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}