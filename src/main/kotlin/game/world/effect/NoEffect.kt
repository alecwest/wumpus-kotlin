package game.world.effect

import game.world.RoomContent
import game.world.World
import java.awt.Point

// TODO NoEffect shouldn't have to declare any room content. Delete NoEffect or fix.
class NoEffect: WorldEffect(roomContent = RoomContent.FOOD) {
    override fun applyEffect(world: World, point: Point) {
        // Do nothing
    }

    override fun removeEffect(world: World, point: Point) {
        // Do nothing
    }
}