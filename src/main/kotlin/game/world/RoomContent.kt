package game.world

import game.player.InventoryItem
import game.world.GameObjectCharacteristic.*
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
 * Dangerous1 objects are RoomContent that are intended to kill players on contact
 */
sealed class Dangerous1(weaknesses: Set<InventoryItem> = setOf()): RoomContent(weaknesses) {
    object PIT : Dangerous1()
    object SUPMUW_EVIL : Dangerous1(setOf(InventoryItem.ARROW))
    object SUPMUW : Dangerous1(setOf(InventoryItem.ARROW))
    object WUMPUS : Dangerous1(setOf(InventoryItem.ARROW))
}

fun roomContentValues(): List<RoomContent> {
    return RoomContent::class.nestedClasses.map { it.objectInstance as RoomContent } + dangerousValues()
}

fun dangerousValues(): List<RoomContent> {
    return Dangerous1::class.nestedClasses.map { it.objectInstance as RoomContent }
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
        Dangerous1.PIT -> arrayListOf(AdjacentEffect(RoomContent.BREEZE))
        Dangerous1.SUPMUW_EVIL -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO))
        Dangerous1.SUPMUW -> arrayListOf(AdjacentEffect(RoomContent.MOO),
                DiagonalEffect(RoomContent.MOO), HereEffect(RoomContent.FOOD))
        Dangerous1.WUMPUS -> arrayListOf(AdjacentEffect(RoomContent.STENCH))
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
        Dangerous1.PIT -> "P"
        Dangerous1.SUPMUW_EVIL -> "E"
        Dangerous1.SUPMUW -> "S"
        Dangerous1.WUMPUS -> "W"
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
        Dangerous1.PIT.toCharRepresentation() -> Dangerous1.PIT
        Dangerous1.SUPMUW_EVIL.toCharRepresentation() -> Dangerous1.SUPMUW_EVIL
        Dangerous1.SUPMUW.toCharRepresentation() -> Dangerous1.SUPMUW
        Dangerous1.WUMPUS.toCharRepresentation() -> Dangerous1.WUMPUS
        else -> throw Exception("Cannot convert %s to a RoomContent value".format(this))
    }
}

sealed class GameObject(val characeteristics: Set<GameObjectCharacteristic> = setOf()) {
    object ARROW : GameObject(setOf(Shootable(), Grabbable()))
    object BLOCKADE : GameObject(setOf(Mappable("X"), Perceptable(), Blocking()))
    object BREEZE : GameObject(setOf(Mappable("="), Perceptable()))
    object FOOD : GameObject(setOf(Mappable("F"), Grabbable(), Perceptable()))
    object GLITTER : GameObject(setOf(Mappable("*"), Perceptable()))
    object GOLD : GameObject(setOf(Mappable("G"), Grabbable()))
    object MOO : GameObject(setOf(Mappable("!"), Perceptable()))
    object PIT : GameObject(setOf(Dangerous(), Mappable("P"), WorldAffecting(setOf(AdjacentEffect(RoomContent.BREEZE)))))
    object STENCH : GameObject(setOf(Mappable("~"), Perceptable()))
    object SUPMUW : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("S"), WorldAffecting(
            setOf(AdjacentEffect(RoomContent.MOO), DiagonalEffect(RoomContent.MOO), HereEffect(RoomContent.FOOD)))))
    object SUPMUW_EVIL : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("E"), WorldAffecting(
            setOf(AdjacentEffect(RoomContent.MOO), DiagonalEffect(RoomContent.MOO)))))
    object WUMPUS : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("W"), WorldAffecting(
            setOf(AdjacentEffect(RoomContent.STENCH)))))

    fun hasCharacteristic(characteristic: GameObjectCharacteristic): Boolean {
        return this.characeteristics.any { it::class == characteristic::class }
    }
}

sealed class GameObjectCharacteristic {
    class Blocking: GameObjectCharacteristic()
    class Dangerous: GameObjectCharacteristic()
    class Destructable(val weaknesses: Set<GameObject>): GameObjectCharacteristic()
    class Grabbable: GameObjectCharacteristic()
    class Mappable(val character: String): GameObjectCharacteristic()
    class Perceptable(): GameObjectCharacteristic()
    class Shootable(): GameObjectCharacteristic()
    class WorldAffecting(val effects: Set<WorldEffect>): GameObjectCharacteristic()
}
