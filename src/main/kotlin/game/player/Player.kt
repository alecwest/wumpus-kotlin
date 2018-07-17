package game.player

import java.awt.Point

/**
 * Player keeps track of all player attributes and is to be accessed through the game class
 * only by the server.
 */
data class Player(var playerState: PlayerState) {
    fun isAlive() = playerState.alive
    fun getLocation() = playerState.location
    fun getInventory() = playerState.getInventory()
    fun hasItem(item: Inventory) = getNumberOf(item) > 0
    fun getNumberOf(item: Inventory) = getInventory().getOrDefault(item, 0)
}

data class PlayerState(val alive: Boolean, val location: Point, val inventory: PlayerInventory) {
    fun getInventory() = inventory.inventory
}

data class PlayerInventory(val inventory: Map<Inventory, Int>)


enum class Inventory {
    ARROW,
    FOOD,
    GOLD,
}