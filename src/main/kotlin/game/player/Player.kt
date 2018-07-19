package game.player

import util.Direction
import util.copyThis
import java.awt.Point

/**
 * Player keeps track of all player attributes and is to be accessed through the game class
 * only by the server.
 */
data class Player(var playerState: PlayerState = PlayerState()) {
    fun isAlive() = playerState.alive
    fun getLocation() = playerState.location
    fun getDirection() = playerState.facing
    fun getInventory() = playerState.getInventory()
    fun hasItem(item: Inventory) = getNumberOf(item) > 0
    fun getNumberOf(item: Inventory) = getInventory().getOrDefault(item, 0)
}

data class PlayerState(val alive: Boolean = true, val location: Point = Point(0, 0),
                       val facing: Direction = Direction.NORTH,
                       val inventory: PlayerInventory = PlayerInventory(
                               mapOf(Inventory.ARROW to 1))) {
    fun getInventory() = inventory.inventory

    fun copyThis(alive: Boolean = this.alive, location: Point = this.location.copyThis(),
                         facing: Direction = this.facing, inventory: PlayerInventory = this.inventory.copyThis())
            = PlayerState(alive, location, facing, inventory)
}

data class PlayerInventory(val inventory: Map<Inventory, Int> = mapOf())

fun PlayerInventory.copyThis(inventory: Map<Inventory, Int> = this.inventory) = PlayerInventory(inventory)

enum class Inventory {
    ARROW,
    FOOD,
    GOLD,
}