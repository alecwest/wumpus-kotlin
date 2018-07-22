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
