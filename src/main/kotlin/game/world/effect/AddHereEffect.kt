package game.world.effect

import game.world.RoomContent
import game.world.WorldState
import java.awt.Point

class AddHereEffect(private val roomContent: RoomContent): WorldEffect {
    override fun applyEffect(worldState: WorldState, point: Point) {
        worldState.addRoomContent(point, roomContent)
    }
}