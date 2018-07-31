package game.world.effect

import game.world.RoomContent
import game.world.World
import java.awt.Point

class HereEffect(roomContent: RoomContent): WorldEffect(roomContent) {
    override fun applyEffect(world: World, point: Point) {
        world.addRoomContent(point, getRoomContent())
    }

    override fun removeEffect(world: World, point: Point) {
        world.removeRoomContent(point, getRoomContent())
    }
}