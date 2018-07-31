package game.world.effect

import game.world.RoomContent
import game.world.World
import java.awt.Point

abstract class WorldEffect(private val roomContent: RoomContent) {
    abstract fun applyEffect(world: World, point: Point)
    abstract fun removeEffect(world: World, point: Point)
}