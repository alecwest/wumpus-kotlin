package game

import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.world.World
import util.Direction
import java.awt.Point

/**
 * Game facilitates interactions between a world and its player
 */
data class Game(private val gameState: GameState = GameState()) {
    fun getGameState() = gameState
    fun getGameActive() = gameState.getActive()
    fun gameOver() = gameState.gameOver()

    fun getGameWorld() = gameState.getWorld()

    fun getGamePlayer() = gameState.getPlayer()
    fun isPlayerAlive() = gameState.isPlayerAlive()
    fun getPlayerLocation() = gameState.getPlayerLocation()
    fun getPlayerDirection() = gameState.getPlayerDirection()
    fun getPlayerInventory() = gameState.getPlayerInventory()
    fun addToPlayerInventory(inventoryItem: InventoryItem) = gameState.addToPlayerInventory(inventoryItem)
    fun removeFromPlayerInventory(inventoryItem: InventoryItem) = gameState.removeFromPlayerInventory(inventoryItem)

    fun setPlayerAlive(alive: Boolean) = gameState.setPlayerAlive(alive)
    fun setPlayerLocation(location: Point) = gameState.setPlayerLocation(location)
    fun setPlayerDirection(direction: Direction) = gameState.setPlayerFacing(direction)
    fun setPlayerInventory(inventory: PlayerInventory) = gameState.setPlayerInventory(inventory)
}


data class GameState(private val active: Boolean = true,
                     private val world: World = World(),
                     private val player: Player = Player()) {
    fun getActive() = active
    fun gameOver() = !active

    fun getWorld() = world

    fun getPlayer() = player
    fun isPlayerAlive() = player.isAlive()
    fun getPlayerLocation() = player.getLocation()
    fun getPlayerDirection() = player.getDirection()
    fun getPlayerInventory() = player.getInventory()
    fun addToPlayerInventory(inventoryItem: InventoryItem) = player.addToInventory(inventoryItem)
    fun removeFromPlayerInventory(inventoryItem: InventoryItem) = player.removeFromInventory(inventoryItem)
    fun setPlayerAlive(alive: Boolean) = player.setAlive(alive)
    fun setPlayerLocation(location: Point) = player.setLocation(location)
    fun setPlayerFacing(direction: Direction) = player.setFacing(direction)
    fun setPlayerInventory(inventory: PlayerInventory) = player.setInventory(inventory)
}