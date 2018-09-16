package game.world.effect

import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import util.diagonals
import java.awt.Point

abstract class WorldEffect(internal val gameObject: GameObject) {
    open fun applyEffect(world: World, point: Point): Boolean {
        val roomsAffected = roomsAffected(point).filter { world.roomIsValid(it) }
        if (roomsAffected.isEmpty()) return false
        if (roomsAffected.all { world.hasGameObject(it, gameObject) }) return false
        roomsAffected.forEach { affectedPoint ->
            world.addGameObject(affectedPoint, gameObject)
        }
        return true
    }

    open fun removeEffect(world: World, point: Point): Boolean {
        val roomsAffected = roomsAffected(point).filter { world.roomIsValid(it) }
        if (roomsAffected.all { !world.hasGameObject(it, gameObject) }) return false
        if (roomsAffected.isEmpty()) return false
        roomsAffected.forEach { affectedPoint ->
            if (!nearbyContentHasAssociatedEffect(world, affectedPoint)) {
                world.removeGameObject(affectedPoint, gameObject)
                return true
            }
        }
        return false
    }

    abstract fun roomsAffected(point: Point): Set<Point>

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