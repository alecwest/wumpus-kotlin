package game.player

import util.Direction
import java.awt.Point

/**
 * Player keeps track of all player attributes and is to be accessed through the game class
 * only by the server.
 */
data class Player(private var playerState: PlayerState = PlayerState()) {
    fun isAlive() = getPlayerState().isAlive()
    fun getPlayerState() = playerState
    fun getLocation() = getPlayerState().getLocation()
    fun getDirection() = getPlayerState().getDirection()
    fun getInventory() = getPlayerState().getInventory()
    fun hasItem(item: InventoryItem) = getPlayerState().hasItem(item)
    fun getNumberOf(item: InventoryItem) = getPlayerState().getNumberOf(item)
    fun getScore() = getPlayerState().getScore()

    fun addToInventory(inventoryItem: InventoryItem) {
        val newMap = getPlayerState().getInventory().toMutableMap()
        if (inventoryItem in getPlayerState().getInventory().keys) {
            newMap[inventoryItem] = getPlayerState().getInventory().getValue(inventoryItem) + 1
        } else {
            newMap[inventoryItem] = 1
        }
        playerState = getPlayerState().copyThis(inventory = PlayerInventory(newMap.toMap()))
    }

    fun removeFromInventory(inventoryItem: InventoryItem) {
        val newMap = getPlayerState().getInventory().toMutableMap()
        if (getNumberOf(inventoryItem) > 0) {
            newMap[inventoryItem] = newMap.getValue(inventoryItem) - 1
        }
        playerState = getPlayerState().copyThis(inventory = PlayerInventory(newMap.toMap()))
    }

    fun setAlive(alive: Boolean) {
        playerState = getPlayerState().copyThis(alive = alive)
    }

    fun setLocation(location: Point) {
        playerState = getPlayerState().copyThis(location = location)
    }

    fun setFacing(direction: Direction) {
        playerState = getPlayerState().copyThis(facing = direction)
    }

    fun setInventory(inventory: PlayerInventory) {
        playerState = getPlayerState().copyThis(inventory = inventory)
    }

    fun setScore(score: Int) {
        playerState = getPlayerState().copyThis(score = score)
    }
}
