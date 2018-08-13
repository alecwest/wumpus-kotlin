package game

import game.player.InventoryItem
import game.player.PlayerInventory
import game.command.CommandResult
import game.world.GameObject
import game.world.GameObjectFeature.*
import util.Direction
import java.awt.Point

/**
 * Game retrieves the GameState and facilitates its manipulation
 */
data class Game(private var gameState: GameState = GameState()) {
    private var lastCommandResult: CommandResult = CommandResult(
            perceptions = arrayListOf(),
            playerState = getPlayerState())

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
    fun getGameObjects(point: Point = getPlayerLocation()) = gameState.getGameObjects(point)
    fun hasGameObject(point: Point, content: GameObject) = gameState.hasGameObject(point, content)
    fun roomIsValid(point: Point) = gameState.roomIsValid(point)
    fun roomIsEmpty(point: Point) = gameState.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = gameState.getRoomIndex(point)
    fun getWorldMap() = gameState.getWorldMap()
    fun getRoom(point: Point) = gameState.getRoom(point)
    fun getNumberRooms() = gameState.getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = gameState.getAmountOfObjectsInRoom(point)

    fun addToRoom(point: Point, gameObject: GameObject) {
        val newWorld = gameState.getWorld()
        newWorld.addGameObject(point, gameObject)
        gameState = gameState.copyThis(world = newWorld)
    }

    fun removeFromRoom(point: Point, gameObject: GameObject) {
        val newWorld = gameState.getWorld()
        newWorld.removeGameObject(point, gameObject)
        gameState = gameState.copyThis(world = newWorld)
    }

    fun getPlayer() = gameState.getPlayer()
    fun getPlayerState() = gameState.getPlayerState()
    fun isPlayerAlive() = gameState.isPlayerAlive()
    fun getPlayerLocation() = gameState.getPlayerLocation()
    fun getPlayerDirection() = gameState.getPlayerDirection()
    fun getPlayerInventory() = gameState.getPlayerInventory()
    fun playerHasItem(inventoryItem: InventoryItem) = gameState.playerHasItem(inventoryItem)

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
        if (getGameObjects(location).any { it.hasFeature(Dangerous()) }) {
            newPlayer.setAlive(false)
        }
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
