package game

import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.world.RoomContent
import game.world.World
import util.Direction
import java.awt.Point

/**
 * Game retrieves the GameState and facilitates its manipulation
 */
data class Game(private var gameState: GameState = GameState()) {
    fun getGameState() = gameState

    fun getActive() = gameState.getActive()
    fun gameOver() = gameState.gameOver()

    fun getWorld() = gameState.getWorld()
    fun getRooms() = gameState.getRooms()
    fun hasRoomContent(point: Point, content: RoomContent) = gameState.hasRoomContent(point, content)
    fun roomIsEmpty(point: Point) = gameState.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = gameState.getRoomIndex(point)
    fun getWorldMap() = gameState.getWorldMap()
    fun getRoom(point: Point) = gameState.getRoom(point)
    fun getNumberRooms() = gameState.getNumberRooms()
    fun getAmountOfContentInRoom(point: Point) = gameState.getAmountOfContentInRoom(point)


    fun getPlayer() = gameState.getPlayer()
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
