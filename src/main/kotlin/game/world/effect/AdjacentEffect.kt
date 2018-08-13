package game.world.effect

import game.world.GameObject
import util.adjacents
import game.world.World
import java.awt.Point

class AdjacentEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return setOf()
    }

    override fun applyEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            world.addGameObject(adjacentPoint, gameObject)
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            if (!nearbyContentHasAssociatedEffect(world, adjacentPoint)) {
                world.removeGameObject(adjacentPoint, gameObject)
            }
        }
    }
}