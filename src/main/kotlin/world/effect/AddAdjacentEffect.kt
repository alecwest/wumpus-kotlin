package world.effect

import util.adjacents
import world.RoomContent
import world.World
import java.awt.Point

class AddAdjacentEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            world.addRoomContent(adjacentPoint, roomContent)
        }
    }
}