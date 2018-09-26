package game.world.effect

import game.world.GameObject
import util.adjacents
import game.world.World
import java.awt.Point

/**
 * AdjacentEffect applies to all valid rooms adjacent to the given room
 *
 * @param gameObject object to add
 */
class AdjacentEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return point.adjacents().toSet()
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