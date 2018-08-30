package game.player

import util.Direction
import util.copyThis
import java.awt.Point

data class PlayerState(private val alive: Boolean = true,
                       private val location: Point = Point(0, 0),
                       private val facing: Direction = Direction.NORTH,
                       private val inventory: PlayerInventory = PlayerInventory(
                               mapOf(InventoryItem.ARROW to 1)),
                       private val score: Int = 0) {
    fun isAlive() = alive
    fun getLocation() = location
    fun getDirection() = facing
    fun getInventory() = inventory.getInventory()
    fun getScore() = score

    internal fun copyThis(alive: Boolean = this.alive,
                          location: Point = this.location.copyThis(),
                          facing: Direction = this.facing,
                          inventory: PlayerInventory = this.inventory.copyThis(),
                          score: Int = this.score)
            = PlayerState(alive, location, facing, inventory)

    override fun toString(): String {
        return "Alive: $alive\nLocation: $location\nFacing: $facing\nInventory: $inventory"
    }


}
