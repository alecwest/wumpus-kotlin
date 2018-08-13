package game.world.effect

import game.world.GameObject
import game.world.World
import java.awt.Point

class HereEffect(gameObject: GameObject): WorldEffect(gameObject) {
    override fun roomsAffected(point: Point): Set<Point> {
        return setOf(point)
    }

    override fun applyEffect(world: World, point: Point) {
        world.addGameObject(point, gameObject)
    }

    override fun removeEffect(world: World, point: Point) {
        world.removeGameObject(point, gameObject)
    }
}