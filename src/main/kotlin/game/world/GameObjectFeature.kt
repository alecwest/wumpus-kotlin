package game.world

import game.player.InventoryItem
import game.world.effect.DiagonalEffect
import game.world.effect.WorldEffect
import util.adjacents
import util.diagonals
import java.awt.Point
import kotlin.reflect.KClass

/**
 * A characteristic that can be applied to [GameObject]s
 */
sealed class GameObjectFeature {
    /**
     * Blocks a player from entering the room it is in
     */
    class Blocking: GameObjectFeature()

    /**
     * Kills a player if they enter the room it is in
     */
    open class Dangerous: GameObjectFeature() {
        /**
         * Determine whether or not to actually kill the player
         *
         * @param objectLocation location of this GameObject
         * @param world world it applies to
         *
         * @return [Boolean] whether the player has been killed
         */
        open fun killsPlayer(objectLocation: Point, world: World): Boolean {
            return true
        }
    }

    /**
     * Kills a player if it is in proximity to a given object
     *
     * @param proximityTo [GameObject] to check for
     */
    class ConditionallyDangerous(val proximityTo: GameObject): Dangerous() {
        override fun killsPlayer(objectLocation: Point, world: World): Boolean {
            return proximityTo(proximityTo, objectLocation, world)
        }
    }

    /**
     * Object can be destroyed by having a certain object shot at it
     *
     * @param weaknesses set of objects that can destroy this object
     */
    class Destructable(val weaknesses: Set<GameObject> = setOf()): GameObjectFeature()

    /**
     * Object that provides a means of escape from the world to the player
     */
    class Exitable: GameObjectFeature()

    /**
     * Object that can be picked up and added to the player's inventory
     *
     * @param inventoryItem [InventoryItem] associated with the [GameObject] this is applied to
     * @param value value of the item
     */
    class Grabbable(val inventoryItem: InventoryItem? = null, val value: Int = 0): GameObjectFeature()

    /**
     * Object that can be visualized on a printed map
     *
     * @param character The character representation of the object
     */
    class Mappable(val character: String = ""): GameObjectFeature()

    /**
     * Object that can be perceived by the player
     *
     * @param perception [Perception] associated with the [GameObject] this is applied to
     */
    class Perceptable(val perception: Perception? = null): GameObjectFeature()

    /**
     * Object that cannot exist along-side any other objects in a room. A player can still enter the room.
     */
    class RoomFilling: GameObjectFeature()

    /**
     * Object that can be shot by the player
     *
     * @param cost the move cost of shooting this item
     */
    class Shootable(val cost: Int = 1) : GameObjectFeature()

    /**
     * Object that can create [GameObject]s in other nearby rooms
     *
     * @param effects list of [WorldEffect]s that can be applied
     */
    open class WorldAffecting(val effects: ArrayList<WorldEffect> = arrayListOf()): GameObjectFeature() {
        /**
         * Check for a specific [WorldEffect] class in the list of effects
         *
         * @param worldEffect class to check for
         *
         * @return [Boolean] indicating existence of class in effects
         */
        fun hasEffectClass(worldEffect: KClass<out WorldEffect>): Boolean {
            return effects.any { worldEffect == it::class }
        }

        /**
         * Check for a specific [WorldEffect] and its content in the list of effects
         *
         * @param worldEffect effect to check for
         *
         * @return [Boolean] indicating existence of same effect in effects
         */
        fun hasExactEffect(worldEffect: WorldEffect): Boolean {
            return effects.any {
                worldEffect::class == it::class && worldEffect.gameObject.toString() == it.gameObject.toString()
            }
        }

        /**
         * Check if any of the effects can create a certain object
         *
         * @param gameObject object to check for
         *
         * @return [Boolean] indicating the object can be created
         */
        fun createsObject(gameObject: GameObject): Boolean {
            return effects.any {
                it.gameObject.toString() == gameObject.toString()
            }
        }
    }

    /**
     * Check if a specific object exists nearby
     *
     * @param target object to check for
     * @param currLocation central point to search from
     * @param world world this applies to
     *
     * @return [Boolean] indicating proximity
     */
    internal fun proximityTo(target: GameObject, currLocation: Point, world: World): Boolean {
        return (currLocation.adjacents() + currLocation.diagonals()).any {
            world.hasGameObject(it, target)
        }
    }
}
