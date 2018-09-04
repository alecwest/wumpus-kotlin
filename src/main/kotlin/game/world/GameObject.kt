package game.world

import game.player.InventoryItem
import game.world.GameObjectFeature.*
import game.world.effect.*
import kotlin.reflect.full.isSubclassOf

sealed class GameObject(val features: Set<GameObjectFeature> = setOf()) {
    object ARROW : GameObject(setOf(Shootable(10), Grabbable(InventoryItem.ARROW)))
    object BLOCKADE : GameObject(setOf(Blocking(), Mappable("X"), Perceptable(Perception.BLOCKADE_BUMP),
            RoomFilling()))
    object BREEZE : GameObject(setOf(Mappable("="), Perceptable(Perception.BREEZE)))
    object EXIT : GameObject(setOf(Exitable(), Mappable("E"), Perceptable(Perception.EXIT)))
    object FOOD : GameObject(setOf(Mappable("F"), Grabbable(InventoryItem.FOOD, 100), Perceptable(Perception.FOOD)))
    object GLITTER : GameObject(setOf(Mappable("*"), Perceptable(Perception.GLITTER)))
    object GOLD : GameObject(setOf(Mappable("G"), Grabbable(InventoryItem.GOLD, 1000),
            WorldAffecting(arrayListOf(HereEffect(GameObject.GLITTER)))))
    object MOO : GameObject(setOf(Mappable("!"), Perceptable(Perception.MOO)))
    object PIT : GameObject(setOf(Dangerous(), Mappable("P"),
            WorldAffecting(arrayListOf(AdjacentEffect(GameObject.BREEZE)))))
    object STENCH : GameObject(setOf(Mappable("~"), Perceptable(Perception.STENCH)))
    object SUPMUW : GameObject(setOf(ConditionallyDangerous(GameObject.WUMPUS), Destructable(setOf(GameObject.ARROW)),
            Mappable("S"), WorldAffecting(arrayListOf(AdjacentEffect(GameObject.MOO),
            DiagonalEffect(GameObject.MOO), HereEffect(GameObject.FOOD)))))
    object WALL : GameObject(setOf(Blocking(), Perceptable(Perception.WALL_BUMP), RoomFilling()))
    object WUMPUS : GameObject(setOf(Dangerous(), Destructable(setOf(GameObject.ARROW)), Mappable("W"),
            WorldAffecting(arrayListOf(AdjacentEffect(GameObject.STENCH)))))

    fun getFeature(feature: GameObjectFeature): GameObjectFeature? {
        return this.features.find { it::class.isSubclassOf(feature::class) }
    }

    fun hasFeature(feature: GameObjectFeature): Boolean {
        return this.features.any { it::class.isSubclassOf(feature::class) }
    }

    fun objectsThatCreateThis(): List<GameObject> {
        return gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldEffector ->
            (worldEffector.getFeature(WorldAffecting()) as WorldAffecting).createsObject(this) }
    }

    fun toInventoryItem(): InventoryItem? {
        return ((this.objectsThatCreateThis().firstOrNull { it.hasFeature(Grabbable()) }
                ?: this).getFeature(Grabbable()) as Grabbable?)?.inventoryItem
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

fun InventoryItem.toGameObject(): GameObject {
    return gameObjectsWithFeatures(setOf(Grabbable())).first {
        (it.getFeature(Grabbable()) as Grabbable).inventoryItem == this
    }
}
