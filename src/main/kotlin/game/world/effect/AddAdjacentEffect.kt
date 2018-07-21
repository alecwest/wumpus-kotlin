package game.world.effect

import util.adjacents
import game.world.RoomContent
import game.world.WorldState
import java.awt.Point

class AddAdjacentEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(worldState: WorldState, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            worldState.addRoomContent(adjacentPoint, roomContent)
        }
    }
}