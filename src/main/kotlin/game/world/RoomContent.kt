package game.world

import game.world.effect.*
import kotlin.reflect.KClass

/**
 * Changes:
 *      All AGENT types are removed in favor of a data structure containing the client's position, direction faced,
 *      and inventory which will be used to render the agent appropriately on the map.
 */
enum class RoomContent {
    ARROW,
    BLOCKADE,
    BREEZE,
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

fun RoomContent.toPerception(): Perception? {
    return when(this) {
        RoomContent.BLOCKADE -> Perception.BLOCKADE_BUMP
        RoomContent.BREEZE -> Perception.BREEZE
        RoomContent.GLITTER -> Perception.GLITTER
        RoomContent.MOO -> Perception.MOO
        RoomContent.STENCH -> Perception.STENCH
        else -> null
    }
}

fun RoomContent.associatedEffects(): ArrayList<WorldEffect> {
    return when(this) {
        RoomContent.ARROW -> arrayListOf()
        RoomContent.BLOCKADE -> arrayListOf()
        RoomContent.BREEZE -> arrayListOf()
        RoomContent.FOOD -> arrayListOf()
        RoomContent.GLITTER -> arrayListOf()
        RoomContent.GOLD -> arrayListOf(HereEffect(RoomContent.GLITTER))
        RoomContent.MOO -> arrayListOf()
        RoomContent.PIT -> arrayListOf(AdjacentEffect(RoomContent.BREEZE))
        RoomContent.STENCH -> arrayListOf()
        RoomContent.SUPMUW_EVIL -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO))
        RoomContent.SUPMUW -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO), HereEffect(RoomContent.FOOD))
        RoomContent.WUMPUS -> arrayListOf(AdjacentEffect(RoomContent.STENCH))
    }
}

fun RoomContent.toCharRepresentation(): String {
    return when(this){
        RoomContent.ARROW -> "A"
        RoomContent.BLOCKADE -> "X"
        RoomContent.BREEZE -> "="
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
        RoomContent.ARROW.toCharRepresentation() -> RoomContent.ARROW
        RoomContent.BLOCKADE.toCharRepresentation() -> RoomContent.BLOCKADE
        RoomContent.BREEZE.toCharRepresentation() -> RoomContent.BREEZE
        RoomContent.FOOD.toCharRepresentation() -> RoomContent.FOOD
        RoomContent.GLITTER.toCharRepresentation() -> RoomContent.GLITTER
        RoomContent.GOLD.toCharRepresentation() -> RoomContent.GOLD
        RoomContent.MOO.toCharRepresentation() -> RoomContent.MOO
        RoomContent.PIT.toCharRepresentation() -> RoomContent.PIT
        RoomContent.STENCH.toCharRepresentation() -> RoomContent.STENCH
        RoomContent.SUPMUW_EVIL.toCharRepresentation() -> RoomContent.SUPMUW_EVIL
        RoomContent.SUPMUW.toCharRepresentation() -> RoomContent.SUPMUW
        RoomContent.WUMPUS.toCharRepresentation() -> RoomContent.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}