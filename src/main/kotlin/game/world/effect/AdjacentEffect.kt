package game.world.effect

import game.world.GameObject
import util.adjacents
import game.world.World
import java.awt.Point

class AdjacentEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return point.adjacents().toSet()
    }

    override fun applyEffect(world: World, point: Point) {
        for (adjacentPoint in roomsAffected(point)) {
            world.addGameObject(adjacentPoint, gameObject)
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in roomsAffected(point)) {
            if (!nearbyContentHasAssociatedEffect(world, adjacentPoint)) {
                world.removeGameObject(adjacentPoint, gameObject)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdjacentEffect) return false
        if (gameObject != other.gameObject) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}