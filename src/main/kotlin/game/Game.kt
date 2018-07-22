package game

import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.world.World
import util.Direction
import java.awt.Point

/**
 * Game retrieves the GameState and facilitates its manipulation
 */
data class Game(private var gameState: GameState = GameState()) {
    fun getGameState() = gameState

    fun getGameActive() = gameState.getActive()
    fun gameOver() = gameState.gameOver()

    fun getGameWorld() = gameState.getWorld()

    fun getGamePlayer() = gameState.getPlayer()
    fun isPlayerAlive() = gameState.isPlayerAlive()
    fun getPlayerLocation() = gameState.getPlayerLocation()
    fun getPlayerDirection() = gameState.getPlayerDirection()
    fun getPlayerInventory() = gameState.getPlayerInventory()

    fun addToPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = gameState.getPlayer()
        newPlayer.addToInventory(inventoryItem)
        gameState = gameState.copyThis(player = newPlayer)
    }
    fun removeFromPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = gameState.getPlayer()
        newPlayer.removeFromInventory(inventoryItem)
        gameState = gameState.copyThis(player = newPlayer)
    }

    fun setPlayerAlive(alive: Boolean) {
        val newPlayer = gameState.getPlayer()
        newPlayer.setAlive(alive)
        gameState = gameState.copyThis(player = newPlayer)
    }
    fun setPlayerLocation(location: Point) {
        val newPlayer = gameState.getPlayer()
        newPlayer.setLocation(location)
        gameState = gameState.copyThis(player = newPlayer)
    }

    fun setPlayerDirection(direction: Direction) {
        val newPlayer = gameState.getPlayer()
        newPlayer.setFacing(direction)
        gameState = gameState.copyThis(player = newPlayer)
    }

    fun setPlayerInventory(inventory: PlayerInventory) {
        val newPlayer = gameState.getPlayer()
        newPlayer.setInventory(inventory)
        gameState = gameState.copyThis(player = newPlayer)
    }
}
