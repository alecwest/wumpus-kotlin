package game.world.effect

import game.world.GameObject
import util.diagonals
import game.world.World
import java.awt.Point

class DiagonalEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return point.diagonals().toSet()
    }

    override fun applyEffect(world: World, point: Point) {
        for (diagonalPoint in roomsAffected(point)) {
            world.addGameObject(diagonalPoint, gameObject)
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in roomsAffected(point)) {
            if (!nearbyContentHasAssociatedEffect(world, adjacentPoint)) {
                world.removeGameObject(adjacentPoint, gameObject)
            }
        }
    }
}