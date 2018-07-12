package world.effect

import world.RoomContent
import world.World
import java.awt.Point

class AddHereEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(world: World, point: Point) {
        world.addRoomContent(point, roomContent)
    }
}