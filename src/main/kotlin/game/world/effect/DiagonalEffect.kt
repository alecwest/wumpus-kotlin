package game.world.effect

import util.diagonals
import game.world.RoomContent
import game.world.World
import util.adjacents
import java.awt.Point

class DiagonalEffect(roomContent: RoomContent): WorldEffect(roomContent) {
    override fun applyEffect(world: World, point: Point) {
        for (diagonalPoint in point.diagonals()) {
            world.addRoomContent(diagonalPoint, getRoomContent())
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in point.diagonals()) {
            if (!nearbyContentHasAssociatedEffect(world, adjacentPoint)) {
                world.removeRoomContent(adjacentPoint, getRoomContent())
            }
        }
    }
}