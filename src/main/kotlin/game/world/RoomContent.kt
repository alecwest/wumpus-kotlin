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
    WUMPUS
}

fun RoomContent.toCharRepresentation(): String {
    return when(this){
        RoomContent.ARROW -> "A"
        RoomContent.BLOCKADE -> "X"
        RoomContent.BREEZE -> "="
        RoomContent.BUMP -> " "
        RoomContent.FOOD -> "F"
        RoomContent.GLITTER -> "*"
        RoomContent.GOLD -> "G"
        RoomContent.MOO -> "!"
        RoomContent.PIT -> "O"
        RoomContent.STENCH -> "~"
        RoomContent.SUPMUW_EVIL -> "E"
        RoomContent.SUPMUW -> "S"
        RoomContent.WUMPUS -> "W"
    }
}

fun String.toRoomContent(): RoomContent {
    return when(this) {
        "A" -> RoomContent.ARROW
        "X" -> RoomContent.BLOCKADE
        "=" -> RoomContent.BREEZE
        " " -> RoomContent.BUMP
        "F" -> RoomContent.FOOD
        "*" -> RoomContent.GLITTER
        "G" -> RoomContent.GOLD
        "!" -> RoomContent.MOO
        "O" -> RoomContent.PIT
        "~" -> RoomContent.STENCH
        "E" -> RoomContent.SUPMUW_EVIL
        "S" -> RoomContent.SUPMUW
        "W" -> RoomContent.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}