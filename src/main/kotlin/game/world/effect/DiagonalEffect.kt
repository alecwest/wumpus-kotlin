package game.world.effect

import game.world.GameObject
import util.diagonals
import game.world.World
import java.awt.Point

class DiagonalEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return point.diagonals().toSet()
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
        if (other !is DiagonalEffect) return false
        if (gameObject != other.gameObject) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}