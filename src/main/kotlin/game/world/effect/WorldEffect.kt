package game.world.effect

import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import util.diagonals
import java.awt.Point

/**
 * WorldEffect is a side effect applied upon addition of certain [GameObject]s to a world
 *
 * @param gameObject object to add
 */
abstract class WorldEffect(internal val gameObject: GameObject) {
    /**
     * Apply this effect to the applicable areas
     *
     * @param world world to apply to
     * @param point center of application area
     * @return [Boolean] indicating whether or not anything was added
     */
    open fun applyEffect(world: World, point: Point): Boolean {
        val roomsAffected = roomsAffected(point).filter { world.roomIsValid(it) }
        if (roomsAffected.isEmpty()) return false
        if (roomsAffected.all { world.hasGameObject(it, gameObject) }) return false
        roomsAffected.forEach { affectedPoint ->
            world.addGameObject(affectedPoint, gameObject)
        }
        return true
    }

    /**
     * Remove this effect from the applicable areas
     *
     * @param world world to apply to
     * @param point center of the application area
     * @return [Boolean] indicating whether or not anything was removed
     */
    open fun removeEffect(world: World, point: Point): Boolean {
        val roomsAffected = getRoomsAffected(world, point)
        if (noEffectToRemove(world, point)) return false
        var result = false
        roomsAffected.forEach { affectedPoint ->
            if (!nearbyContentHasAssociatedEffect(world, affectedPoint)) {
                world.removeGameObject(affectedPoint, gameObject)
                result = true
            }
        }
        return result
    }

    /**
     * Get all rooms that the effect should be applied to
     *
     * @param point center of the application area
     * @return [Set] affected rooms
     */
    abstract fun roomsAffected(point: Point): Set<Point>

    /**
     * Check if nothing is nearby to remove
     *
     * @param world world to apply to
     * @param point center of the application area
     *
     * @return [Boolean] indicating whether or not there is anything to remove nearby
     */
    internal fun noEffectToRemove(world: World, point: Point): Boolean {
        val roomsAffected = getRoomsAffected(world, point)
        if (roomsAffected.isEmpty() ||
                roomsAffected.all { !world.hasGameObject(it, gameObject) }) {
            return true
        }
        return false
    }

    /**
     * @param world world to apply to
     * @param point center of the application area
     *
     * @return [List] rooms that area affected by this effect
     */
    internal fun getRoomsAffected(world: World, point: Point): List<Point> {
        return roomsAffected(point).filter { world.roomIsValid(it) }
    }

    /**
     * Check nearby rooms for objects that are creating similar [GameObject] effects
     *
     * @param world world to apply to
     * @param point center of the application area
     *
     * @return [Boolean]
     */
    fun nearbyContentHasAssociatedEffect(world: World, point: Point): Boolean {
        for (adjacentPoint in point.adjacents() + point.diagonals()) {
            for (content in gameObjectsWithFeatures(setOf(WorldAffecting()))) {
                for (effect in (content.getFeature(WorldAffecting()) as WorldAffecting).effects) {
                    if (effect.gameObject == gameObject && world.hasGameObject(adjacentPoint, content)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}