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

// TODO test this and swap out function in World
fun RoomContent.associatedEffects(): Map<out KClass<out WorldEffect>, ArrayList<RoomContent>> {
    return when(this) {
        RoomContent.ARROW -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.BLOCKADE -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.BREEZE -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.FOOD -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.GLITTER -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.GOLD -> mapOf(HereEffect::class to arrayListOf(RoomContent.GLITTER))
        RoomContent.MOO -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.PIT -> mapOf(AdjacentEffect::class to arrayListOf(RoomContent.BREEZE))
        RoomContent.STENCH -> mapOf(NoEffect::class to arrayListOf())
        RoomContent.SUPMUW_EVIL -> mapOf(AdjacentEffect::class to arrayListOf(RoomContent.MOO),
                DiagonalEffect::class to arrayListOf(RoomContent.MOO))
        RoomContent.SUPMUW -> mapOf(AdjacentEffect::class to arrayListOf(RoomContent.MOO),
                DiagonalEffect::class to arrayListOf(RoomContent.MOO),
                HereEffect::class to arrayListOf(RoomContent.FOOD))
        RoomContent.WUMPUS -> mapOf(AdjacentEffect::class to arrayListOf(RoomContent.STENCH))
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