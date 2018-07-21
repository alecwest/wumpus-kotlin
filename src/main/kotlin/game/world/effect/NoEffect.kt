package game.world.effect

import game.world.WorldState
import java.awt.Point

class NoEffect: WorldEffect {
    override fun applyEffect(worldState: WorldState, point: Point) {
        // Do nothing
    }
}