package game.world

import game.player.InventoryItem
import game.world.effect.*

/**
 * RoomContent is a base class for all permanent/semi-permanent objects in the world
 */
sealed class RoomContent(val weaknesses: Set<InventoryItem> = setOf()) {
    object ARROW : RoomContent()
    object BLOCKADE : RoomContent()
    object BREEZE : RoomContent()
    object FOOD : RoomContent()
    object GLITTER : RoomContent()
    object GOLD : RoomContent()
    object MOO : RoomContent()
    object STENCH : RoomContent()

    fun hasWeakness(inventoryItem: InventoryItem) = weaknesses.contains(inventoryItem)
}

/**
 * Dangerous objects are RoomContent that are intended to kill players on contact
 */
sealed class Dangerous(weaknesses: Set<InventoryItem> = setOf()): RoomContent(weaknesses) {
    object PIT : Dangerous()
    object SUPMUW_EVIL : Dangerous(setOf(InventoryItem.ARROW))
    object SUPMUW : Dangerous(setOf(InventoryItem.ARROW))
    object WUMPUS : Dangerous(setOf(InventoryItem.ARROW))
}

fun roomContentValues(): List<RoomContent> {
    return RoomContent::class.nestedClasses.map { it.objectInstance as RoomContent } + dangerousValues()
}

fun dangerousValues(): List<RoomContent> {
    return Dangerous::class.nestedClasses.map { it.objectInstance as RoomContent }
}

/**
 * Intended to inform the client that they could not enter a room because of a certain RoomContent,
 * indicated by the returned Perception
 */
fun RoomContent.toPerception(): Perception? {
    return when(this) {
        RoomContent.BLOCKADE -> Perception.BLOCKADE_BUMP
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
        RoomContent.STENCH -> arrayListOf()
        Dangerous.PIT -> arrayListOf(AdjacentEffect(RoomContent.BREEZE))
        Dangerous.SUPMUW_EVIL -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO))
        Dangerous.SUPMUW -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO), HereEffect(RoomContent.FOOD))
        Dangerous.WUMPUS -> arrayListOf(AdjacentEffect(RoomContent.STENCH))
    }
}

fun RoomContent.toCharRepresentation(): String {
    return when(this){
        RoomContent.ARROW -> "A"
        RoomContent.BLOCKADE -> "B"
        RoomContent.BREEZE -> "="
        RoomContent.FOOD -> "F"
        RoomContent.GLITTER -> "*"
        RoomContent.GOLD -> "G"
        RoomContent.MOO -> "!"
        RoomContent.STENCH -> "~"
        Dangerous.PIT -> "P"
        Dangerous.SUPMUW_EVIL -> "E"
        Dangerous.SUPMUW -> "S"
        Dangerous.WUMPUS -> "W"
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
        RoomContent.STENCH.toCharRepresentation() -> RoomContent.STENCH
        Dangerous.PIT.toCharRepresentation() -> Dangerous.PIT
        Dangerous.SUPMUW_EVIL.toCharRepresentation() -> Dangerous.SUPMUW_EVIL
        Dangerous.SUPMUW.toCharRepresentation() -> Dangerous.SUPMUW
        Dangerous.WUMPUS.toCharRepresentation() -> Dangerous.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}