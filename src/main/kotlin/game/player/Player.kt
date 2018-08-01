package game.player

import util.Direction
import java.awt.Point
import java.util.logging.Logger

/**
 * Player keeps track of all player attributes and is to be accessed through the game class
 * only by the game.server.
 */
data class Player(private var playerState: PlayerState = PlayerState()) {
    private val log = Logger.getLogger(Player::class.qualifiedName)
    fun isAlive() = playerState.isAlive()
    fun getPlayerState() = playerState
    fun getLocation() = playerState.getLocation()
    fun getDirection() = playerState.getDirection()
    fun getInventory() = playerState.getInventory()
    fun hasItem(item: InventoryItem) = getNumberOf(item) > 0
    fun getNumberOf(item: InventoryItem) = getInventory().getOrDefault(item, 0)

    fun addToInventory(inventoryItem: InventoryItem) {
        val newMap = playerState.getInventory().toMutableMap()
        if (inventoryItem in playerState.getInventory().keys) {
            newMap[inventoryItem] = playerState.getInventory().getValue(inventoryItem) + 1
        } else {
            newMap[inventoryItem] = 1
        }
        playerState = playerState.copyThis(inventory = PlayerInventory(newMap.toMap()))
    }

    fun removeFromInventory(inventoryItem: InventoryItem) {
        val newMap = playerState.getInventory().toMutableMap()
        if (getNumberOf(inventoryItem) > 0) {
            newMap[inventoryItem] = newMap.getValue(inventoryItem) - 1
        } else {
            log.info("Item %s was not removed from inventory that doesn't contain it".format(inventoryItem.toString()))
        }
        playerState = playerState.copyThis(inventory = PlayerInventory(newMap.toMap()))
    }

    fun setAlive(alive: Boolean) {
        playerState = playerState.copyThis(alive = alive)
    }

    fun setLocation(location: Point) {
        playerState = playerState.copyThis(location = location)
    }

    fun setFacing(direction: Direction) {
        playerState = playerState.copyThis(facing = direction)
    }

    fun setInventory(inventory: PlayerInventory) {
        playerState = playerState.copyThis(inventory = inventory)
    }
}
