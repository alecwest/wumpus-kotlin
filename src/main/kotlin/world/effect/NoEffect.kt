package world.effect

import world.World
import java.awt.Point

class NoEffect: WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        // Do nothing
    }
}