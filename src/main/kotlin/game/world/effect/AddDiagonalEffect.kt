package game.world.effect

import util.diagonals
import game.world.RoomContent
import game.world.World
import java.awt.Point

class AddDiagonalEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        for (diagonalPoint in point.diagonals()) {
            world.addRoomContent(diagonalPoint, roomContent)
        }
    }

    override fun removeEffect(world: World, point: Point) {
    }
}