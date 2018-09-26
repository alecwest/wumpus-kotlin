package game.player

import util.Direction
import util.copyThis
import java.awt.Point

/**
 * PlayerState is an immutable instance containing data about the player at a single point in time
 *
 * @param alive current alive/dead state of player
 * @param location current [Point] the player is at
 * @param facing current [Direction] the player is facing
 * @param inventory current contents in the player's inventory
 * @param score current score
 */
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
    fun hasItem(item: InventoryItem) = getNumberOf(item) > 0
    fun getNumberOf(item: InventoryItem) = getInventory().getOrDefault(item, 0)
    fun getScore() = score

    internal fun copyThis(alive: Boolean = this.alive,
                          location: Point = this.location.copyThis(),
                          facing: Direction = this.facing,
                          inventory: PlayerInventory = this.inventory.copyThis(),
                          score: Int = this.score)
            = PlayerState(alive, location, facing, inventory, score)

    override fun toString(): String {
        return "Alive: $alive\nLocation: $location\nFacing: $facing\nInventory: $inventory\nScore: $score"
    }


}
