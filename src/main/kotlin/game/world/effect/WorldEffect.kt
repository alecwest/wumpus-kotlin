package game.world.effect

import game.world.World
import java.awt.Point

interface WorldEffect {
    fun applyEffect(world: World, point: Point)
    fun removeEffect(world: World, point: Point)
}