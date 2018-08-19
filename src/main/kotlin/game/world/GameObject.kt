package game.world

import game.player.InventoryItem
import game.world.GameObjectFeature.*
import game.world.effect.*

sealed class GameObject(val features: Set<GameObjectFeature> = setOf()) {
    object ARROW : GameObject(setOf(Shootable(), Grabbable(InventoryItem.ARROW)))
    object BLOCKADE : GameObject(setOf(Blocking(), Mappable("X"), Perceptable(Perception.BLOCKADE_BUMP), RoomFilling()))
    object BREEZE : GameObject(setOf(Mappable("="), Perceptable(Perception.BREEZE)))
    object FOOD : GameObject(setOf(Mappable("F"), Grabbable(InventoryItem.FOOD), Perceptable(Perception.FOOD)))
    object GLITTER : GameObject(setOf(Mappable("*"), Perceptable(Perception.GLITTER)))
    object GOLD : GameObject(setOf(Mappable("G"), Grabbable(InventoryItem.GOLD), WorldAffecting(arrayListOf(HereEffect(GameObject.GLITTER)))))
    object MOO : GameObject(setOf(Mappable("!"), Perceptable(Perception.MOO)))
    object PIT : GameObject(setOf(Dangerous(), Mappable("P"), WorldAffecting(arrayListOf(AdjacentEffect(GameObject.BREEZE)))))
    object STENCH : GameObject(setOf(Mappable("~"), Perceptable(Perception.STENCH)))
    object SUPMUW : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("S"), WorldAffecting(
            arrayListOf(AdjacentEffect(GameObject.MOO), DiagonalEffect(GameObject.MOO), HereEffect(GameObject.FOOD)))))
    object WUMPUS : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("W"), WorldAffecting(
            arrayListOf(AdjacentEffect(GameObject.STENCH)))))

    fun getFeature(feature: GameObjectFeature): GameObjectFeature? {
        return this.features.find { it::class == feature::class }
    }

    fun hasFeature(feature: GameObjectFeature): Boolean {
        return this.features.any { it::class == feature::class }
    }

    fun objectsThatCreateThis(): List<GameObject> {
        return gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldEffector ->
            (worldEffector.getFeature(WorldAffecting()) as WorldAffecting).createsObject(this) }
    }
}

fun gameObjectsWithFeatures(features: Set<GameObjectFeature>): List<GameObject> {
    return gameObjectValues().filter {
        var result = true
        for (feature in features) {
            if (!it.hasFeature(feature)) {
                result = false
                break
            }
        }
        result
    }
}

fun gameObjectValues(): List<GameObject> {
    return GameObject::class.nestedClasses.map { it.objectInstance as GameObject }
}

fun String.toMappableGameObject(): GameObject? {
    return gameObjectsWithFeatures(setOf(Mappable())).find {
        this == (it.getFeature(Mappable()) as Mappable).character
    }
}

fun Perception.toGameObject(): GameObject? {
    for (gameObject in gameObjectsWithFeatures(setOf(Perceptable()))) {
        if ((gameObject.getFeature(Perceptable()) as Perceptable).perception == this) {
            return gameObject
        }
    }
    return null
}

sealed class GameObjectFeature {
    class Blocking: GameObjectFeature() // For things that block a player from entering
    class Dangerous: GameObjectFeature()
    class Destructable(val weaknesses: Set<GameObject> = setOf()): GameObjectFeature()
    class Grabbable(val inventoryItem: InventoryItem? = null): GameObjectFeature()
    class Mappable(val character: String = ""): GameObjectFeature()
    class Perceptable(val perception: Perception? = null): GameObjectFeature()
    class RoomFilling: GameObjectFeature() // For things that must exist in a Room alone
    class Shootable : GameObjectFeature()
    class WorldAffecting(val effects: ArrayList<WorldEffect> = arrayListOf()): GameObjectFeature() {
        fun hasEffect(worldEffect: WorldEffect): Boolean {
            return effects.any {
                worldEffect::class == it::class && worldEffect.gameObject.toString() == it.gameObject.toString()
            }
        }

        fun createsObject(gameObject: GameObject): Boolean {
            return effects.any {
                it.gameObject.toString() == gameObject.toString()
            }
        }
    }
}
