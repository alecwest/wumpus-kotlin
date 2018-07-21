package game.world.effect

import util.diagonals
import game.world.RoomContent
import game.world.WorldState
import java.awt.Point

class AddDiagonalEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(worldState: WorldState, point: Point) {
        for (diagonalPoint in point.diagonals()) {
            worldState.addRoomContent(diagonalPoint, roomContent)
        }
    }
}