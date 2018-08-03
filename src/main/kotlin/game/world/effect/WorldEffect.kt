package game.world.effect

import game.world.RoomContent
import game.world.World
import game.world.associatedEffects
import game.world.roomContentValues
import util.adjacents
import util.diagonals
import java.awt.Point

abstract class WorldEffect(internal val roomContent: RoomContent) {
    abstract fun applyEffect(world: World, point: Point)
    abstract fun removeEffect(world: World, point: Point)

    // TODO really? A triple-nested loop? Gross
    fun nearbyContentHasAssociatedEffect(world: World, point: Point): Boolean {
        for (adjacentPoint in point.adjacents() + point.diagonals()) {
            for (content in roomContentValues()) {
                for (effects in content.associatedEffects()) {
                    if (effects.roomContent == roomContent && world.hasRoomContent(adjacentPoint, content)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}