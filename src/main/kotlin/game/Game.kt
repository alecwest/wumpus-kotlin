package game

import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.RoomContent
import game.command.CommandResult
import util.Direction
import java.awt.Point

/**
 * Game retrieves the GameState and facilitates its manipulation
 */
data class Game(private var gameState: GameState = GameState()) {
    private var lastCommandResult: CommandResult? = null

    fun getGameState() = gameState

    fun getCommandResult() = lastCommandResult
    fun setCommandResult(commandResult: CommandResult) {
        this.lastCommandResult = commandResult
    }

    fun getActive() = gameState.getActive()
    fun gameOver() = gameState.gameOver()

    fun getWorld() = gameState.getWorld()
    fun getWorldSize() = gameState.getWorldSize()
    fun getRooms() = gameState.getRooms()
    fun getRoomContent(point: Point) = gameState.getRoomContent(point)
    fun hasRoomContent(point: Point, content: RoomContent) = gameState.hasRoomContent(point, content)
    fun roomIsValid(point: Point) = gameState.roomIsValid(point)
    fun roomIsEmpty(point: Point) = gameState.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = gameState.getRoomIndex(point)
    fun getWorldMap() = gameState.getWorldMap()
    fun getRoom(point: Point) = gameState.getRoom(point)
    fun getNumberRooms() = gameState.getNumberRooms()
    fun getAmountOfContentInRoom(point: Point) = gameState.getAmountOfContentInRoom(point)

    fun addToRoom(point: Point, roomContent: RoomContent) {
        val newWorld = gameState.getWorld()
        newWorld.addRoomContent(point, roomContent)
        gameState = gameState.copyThis(world = newWorld)
    }

    fun removeFromRoom(point: Point, roomContent: RoomContent) {
        val newWorld = gameState.getWorld()
        newWorld.removeRoomContent(point, roomContent)
        gameState = gameState.copyThis(world = newWorld)
    }

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
