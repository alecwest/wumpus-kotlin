package game.world.effect

import game.world.WorldState
import java.awt.Point

interface WorldEffect {
    fun applyEffect(worldState: WorldState, point: Point)
}