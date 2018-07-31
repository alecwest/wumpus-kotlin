package game.world.effect

import game.world.RoomContent
import game.world.World
import game.world.associatedEffects
import util.adjacents
import util.diagonals
import java.awt.Point

abstract class WorldEffect(roomContent: RoomContent) {
    private val roomContent = roomContent
    fun getRoomContent() = roomContent
    abstract fun applyEffect(world: World, point: Point)
    abstract fun removeEffect(world: World, point: Point)

    // TODO really? A triple-nested loop? Gross
    fun nearbyContentHasAssociatedEffect(world: World, point: Point): Boolean {
        for (adjacentPoint in point.adjacents() + point.diagonals()) {
            for (content in RoomContent.values()) {
                for (effects in content.associatedEffects()) {
                    if (effects.getRoomContent() == getRoomContent() && world.hasRoomContent(adjacentPoint, content)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}