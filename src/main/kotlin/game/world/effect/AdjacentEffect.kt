package game.world.effect

import util.adjacents
import game.world.RoomContent
import game.world.World
import java.awt.Point

class AdjacentEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            world.addRoomContent(adjacentPoint, roomContent)
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            world.removeRoomContent(adjacentPoint, roomContent)
        }
    }
}