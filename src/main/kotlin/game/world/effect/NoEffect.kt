package game.world.effect

import game.world.World
import java.awt.Point

class NoEffect: WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        // Do nothing
    }

    override fun removeEffect(world: World, point: Point) {
        // Do nothing
    }
}