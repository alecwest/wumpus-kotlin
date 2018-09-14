package game.world

import game.player.InventoryItem
import game.world.effect.WorldEffect
import util.adjacents
import util.diagonals
import java.awt.Point

sealed class GameObjectFeature {
    class Blocking: GameObjectFeature() // For things that block a player from entering
    open class Dangerous: GameObjectFeature() {
        open fun killsPlayer(objectLocation: Point, world: World): Boolean {
            return true
        }
    }
    class ConditionallyDangerous(val proximityTo: GameObject): Dangerous() {
        override fun killsPlayer(objectLocation: Point, world: World): Boolean {
            return proximityTo(proximityTo, objectLocation, world)
        }
    }
    class Destructable(val weaknesses: Set<GameObject> = setOf()): GameObjectFeature()
    class Exitable: GameObjectFeature()
    class Grabbable(val inventoryItem: InventoryItem? = null, val value: Int = 0): GameObjectFeature()
    class Mappable(val character: String = ""): GameObjectFeature()
    class Perceptable(val perception: Perception? = null): GameObjectFeature()
    class RoomFilling: GameObjectFeature() // For things that must exist in a Room alone
    class Shootable(val cost: Int = 1) : GameObjectFeature()
    open class WorldAffecting(val effects: ArrayList<WorldEffect> = arrayListOf()): GameObjectFeature() {
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

        open fun createsEffect(currLocation: Point, world: World): Boolean {
            return true
        }
    }

    internal fun proximityTo(target: GameObject, currLocation: Point, world: World): Boolean {
        return (currLocation.adjacents() + currLocation.diagonals()).any {
            world.hasGameObject(it, target)
        }
    }
}
