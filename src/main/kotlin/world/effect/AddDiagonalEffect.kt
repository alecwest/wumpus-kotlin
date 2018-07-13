package world.effect

import util.diagonals
import world.RoomContent
import world.World
import java.awt.Point

class AddDiagonalEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        for (diagonalPoint in point.diagonals()) {
            world.addRoomContent(diagonalPoint, roomContent)
        }
    }
}