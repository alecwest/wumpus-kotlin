package game.world

import game.Game
import game.player.InventoryItem
import game.world.effect.WorldEffect
import util.adjacents
import util.diagonals

sealed class GameObjectFeature {
    class Blocking: GameObjectFeature() // For things that block a player from entering
    open class Dangerous: GameObjectFeature() {
        open fun killsPlayer(game: Game): Boolean {
            return true
        }
    }
    class ConditionallyDangerous(val proximityTo: GameObject): Dangerous() {
        override fun killsPlayer(game: Game): Boolean {
            return proximityTo(proximityTo, game)
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

        open fun createsEffect(game: Game): Boolean {
            return true
        }
    }
    class ConditionallyWorldAffecting(effects: ArrayList<WorldEffect> = arrayListOf(),
                                      val proximityTo: GameObject): WorldAffecting(effects) {
        override fun createsEffect(game: Game): Boolean {
            return !proximityTo(proximityTo, game)
        }
    }

    internal fun proximityTo(gameObject: GameObject, game: Game): Boolean {
        val playerLocation = game.getPlayerLocation()
        return (playerLocation.adjacents() + playerLocation.diagonals()).any {
            game.hasGameObject(it, gameObject)
        }
    }
}
