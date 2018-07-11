package world.effect

import world.World
import java.awt.Point

interface WorldEffect {
    fun applyEffect(world: World, point: Point)
}