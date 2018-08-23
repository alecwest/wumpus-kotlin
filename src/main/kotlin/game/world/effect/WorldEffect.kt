package game.world.effect

import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import util.diagonals
import java.awt.Point

abstract class WorldEffect(internal val gameObject: GameObject) {
    abstract fun applyEffect(world: World, point: Point)
    abstract fun removeEffect(world: World, point: Point)
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