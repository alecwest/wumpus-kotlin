package game.world.effect

import util.adjacents
import game.world.RoomContent
import game.world.World
import game.world.associatedEffects
import java.awt.Point

class AdjacentEffect(roomContent: RoomContent): WorldEffect(roomContent) {
    override fun applyEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            world.addRoomContent(adjacentPoint, roomContent)
        }
    }

    override fun removeEffect(world: World, point: Point) {
        for (adjacentPoint in point.adjacents()) {
            if (!nearbyContentHasAssociatedEffect(world, adjacentPoint)) {
                world.removeRoomContent(adjacentPoint, roomContent)
            }
        }
    }
}