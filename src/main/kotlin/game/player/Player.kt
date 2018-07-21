package game.player

import util.Direction
import util.copyThis
import java.awt.Point
import java.util.logging.Logger

/**
 * Player keeps track of all player attributes and is to be accessed through the game class
 * only by the server.
 */
data class Player(private var playerState: PlayerState = PlayerState()) {
    private val log = Logger.getLogger(Player::class.qualifiedName)
    fun isAlive() = playerState.getAlive()
    fun getLocation() = playerState.getLocation()
    fun getDirection() = playerState.getFacing()
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

data class PlayerState(private val alive: Boolean = true,
                       private val location: Point = Point(0, 0),
                       private val facing: Direction = Direction.NORTH,
                       private val inventory: PlayerInventory = PlayerInventory(
                               mapOf(InventoryItem.ARROW to 1))) {
    fun getAlive() = alive
    fun getLocation() = location
    fun getFacing() = facing
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