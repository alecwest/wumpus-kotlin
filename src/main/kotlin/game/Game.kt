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
    init {
        addToRoom(getPlayerLocation(), GameObject.EXIT)
    }

    private var lastCommandResult: CommandResult = CommandResult.createCommandResult(this)

    fun getGameState() = gameState

    fun getCommandResult() = lastCommandResult
    fun setCommandResult(commandResult: CommandResult) {
        this.lastCommandResult = commandResult
    }

    fun getActive() = getGameState().getActive()
    fun gameOver() = getGameState().gameOver() || !isPlayerAlive()

    fun getWorld() = getGameState().getWorld()
    fun getWorldSize() = getGameState().getWorldSize()
    fun getRooms() = getGameState().getRooms()
    fun getGameObjects(point: Point = getPlayerLocation()) = getGameState().getGameObjects(point)
    fun hasGameObject(point: Point, content: GameObject) = getGameState().hasGameObject(point, content)
    fun roomIsValid(point: Point) = getGameState().roomIsValid(point)
    fun roomIsEmpty(point: Point) = getGameState().roomIsEmpty(point)
    fun getRoomIndex(point: Point) = getGameState().getRoomIndex(point)
    fun getWorldMap() = getGameState().getWorldMap()
    fun getRoom(point: Point) = getGameState().getRoom(point)
    fun getNumberRooms() = getGameState().getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = getGameState().getAmountOfObjectsInRoom(point)
    fun getScore() = getGameState().getScore()
    
    fun addToRoom(point: Point, gameObject: GameObject) {
        val newWorld = getGameState().getWorld()
        newWorld.addGameObjectAndEffects(point, gameObject)
        gameState = getGameState().copyThis(world = newWorld)
    }

    fun removeFromRoom(point: Point, gameObject: GameObject) {
        val newWorld = getGameState().getWorld()
        newWorld.removeGameObject(point, gameObject)
        gameState = getGameState().copyThis(world = newWorld)
    }

    fun getPlayer() = getGameState().getPlayer()
    fun getPlayerState() = getGameState().getPlayerState()
    fun isPlayerAlive() = getGameState().isPlayerAlive()
    fun getPlayerLocation() = getGameState().getPlayerLocation()
    fun getPlayerDirection() = getGameState().getPlayerDirection()
    fun getPlayerInventory() = getGameState().getPlayerInventory()
    fun playerHasItem(inventoryItem: InventoryItem) = getGameState().playerHasItem(inventoryItem)

    fun addToPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.addToInventory(inventoryItem)
        gameState = getGameState().copyThis(player = newPlayer)
    }
    fun removeFromPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.removeFromInventory(inventoryItem)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setActive(active: Boolean) {
        gameState = getGameState().copyThis(active)
    }

    fun setPlayerAlive(alive: Boolean) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.setAlive(alive)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerLocation(location: Point) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.setLocation(location)
        if (getGameObjects(location).any { it.hasFeature(Dangerous())
                        && (it.getFeature(Dangerous()) as Dangerous).killsPlayer(location, getWorld())}) {
            setPlayerAlive(false)
        }
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerDirection(direction: Direction) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.setFacing(direction)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerInventory(inventory: PlayerInventory) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.setInventory(inventory)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerScore(score: Int) {
        val newPlayer = getGameState().getPlayer()
        newPlayer.setScore(score)
        gameState = getGameState().copyThis(player = newPlayer)
    }
}
