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
    fun hasItem(item: InventoryItem) = getNumberOf(item) > 0
    fun getNumberOf(item: InventoryItem) = getInventory().getOrDefault(item, 0)
    fun addToInventory(inventoryItem: InventoryItem) {
        val newMap = playerState.inventory.inventoryItems.toMutableMap()
        if (inventoryItem in playerState.inventory.inventoryItems.keys) {
            newMap[inventoryItem] = playerState.inventory.inventoryItems.getValue(inventoryItem) + 1
        } else {
            newMap[inventoryItem] = 1
        }
        playerState = PlayerState(inventory = PlayerInventory(newMap.toMap()))
    }
}

data class PlayerState(val alive: Boolean = true, val location: Point = Point(0, 0),
                       val facing: Direction = Direction.NORTH,
                       val inventory: PlayerInventory = PlayerInventory(
                               mapOf(InventoryItem.ARROW to 1))) {
    fun getInventory() = inventory.inventoryItems

    fun copyThis(alive: Boolean = this.alive, location: Point = this.location.copyThis(),
                         facing: Direction = this.facing, inventory: PlayerInventory = this.inventory.copyThis())
            = PlayerState(alive, location, facing, inventory)
}

data class PlayerInventory(val inventoryItems: Map<InventoryItem, Int> = mapOf())

fun PlayerInventory.copyThis(inventoryItem: Map<InventoryItem, Int> = this.inventoryItems) = PlayerInventory(inventoryItem)

enum class InventoryItem {
    ARROW,
    FOOD,
    GOLD,
}