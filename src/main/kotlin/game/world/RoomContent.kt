package game.world

import game.player.InventoryItem
import game.world.effect.*

/**
 * RoomContent is a base class for all permanent/semi-permanent objects in the world
 */
sealed class RoomContent {
    object ARROW : RoomContent()
    object BLOCKADE : RoomContent()
    object BREEZE : RoomContent()
    object FOOD : RoomContent()
    object GLITTER : RoomContent()
    object GOLD : RoomContent()
    object MOO : RoomContent()
    object PIT : RoomContent()
    object STENCH : RoomContent()
}

/**
 * Destructable objects are RoomContent that have weaknesses
 */
sealed class Destructable(private val weaknesses: Set<InventoryItem>): RoomContent() {
    object SUPMUW_EVIL : Destructable(setOf(InventoryItem.ARROW))
    object SUPMUW : Destructable(setOf(InventoryItem.ARROW))
    object WUMPUS : Destructable(setOf(InventoryItem.ARROW))

    fun hasWeakness(inventoryItem: InventoryItem) = weaknesses.contains(inventoryItem)
}

fun roomContentValues(): List<RoomContent> {
    return RoomContent::class.nestedClasses.map { it.objectInstance as RoomContent } + destructableValues()
}

fun destructableValues(): List<RoomContent> {
    return Destructable::class.nestedClasses.map { it.objectInstance as RoomContent }
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
        RoomContent.PIT -> arrayListOf(AdjacentEffect(RoomContent.BREEZE))
        RoomContent.STENCH -> arrayListOf()
        Destructable.SUPMUW_EVIL -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO))
        Destructable.SUPMUW -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO), HereEffect(RoomContent.FOOD))
        Destructable.WUMPUS -> arrayListOf(AdjacentEffect(RoomContent.STENCH))
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
        RoomContent.PIT -> "P"
        RoomContent.STENCH -> "~"
        Destructable.SUPMUW_EVIL -> "E"
        Destructable.SUPMUW -> "S"
        Destructable.WUMPUS -> "W"
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
        Destructable.SUPMUW_EVIL.toCharRepresentation() -> Destructable.SUPMUW_EVIL
        Destructable.SUPMUW.toCharRepresentation() -> Destructable.SUPMUW
        Destructable.WUMPUS.toCharRepresentation() -> Destructable.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}