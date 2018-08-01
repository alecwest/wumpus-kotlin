package game.player

import util.Direction
import util.copyThis
import java.awt.Point

data class PlayerState(private val alive: Boolean = true,
                       private val location: Point = Point(0, 0),
                       private val facing: Direction = Direction.NORTH,
                       private val inventory: PlayerInventory = PlayerInventory(
                               mapOf(InventoryItem.ARROW to 1))) {
    fun isAlive() = alive
    fun getLocation() = location
    fun getDirection() = facing
    fun getInventory() = inventory.getInventory()

    internal fun copyThis(alive: Boolean = this.alive,
                 location: Point = this.location.copyThis(),
                 facing: Direction = this.facing,
                 inventory: PlayerInventory = this.inventory.copyThis())
            = PlayerState(alive, location, facing, inventory)
}
