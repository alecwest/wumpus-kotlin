package game

import game.player.InventoryItem
import game.player.PlayerInventory
import game.command.CommandResult
import game.world.GameObject
import game.world.GameObjectFeature.*
import util.Direction
import java.awt.Point

/**
 * Game maintains the GameState and facilitates its manipulation
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

    /**
     * Game is over if the player is dead or the Game has been finished
     * @return [Boolean]
     */
    fun gameOver() = getGameState().gameOver() || !isPlayerAlive()

    fun getWorld() = getGameState().getWorld()
    fun getWorldSize() = getGameState().getWorldSize()
    fun getRooms() = getGameState().getRooms()
    fun getGameObjects(point: Point = getPlayerLocation()) = getGameState().getGameObjects(point)

    /**
     * @param point room to check
     * @param content object to check for
     *
     * @return [Boolean] indicating object exists in room
     */
    fun hasGameObject(point: Point, content: GameObject) = getGameState().hasGameObject(point, content)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room is within world boundaries
     */
    fun roomIsValid(point: Point) = getGameState().roomIsValid(point)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room has nothing in it
     */
    fun roomIsEmpty(point: Point) = getGameState().roomIsEmpty(point)

    /**
     * @param point room to get
     *
     * @return [Int] index or room in [World][game.world.World]s array of rooms
     */
    fun getRoomIndex(point: Point) = getGameState().getRoomIndex(point)
    fun getWorldMap() = getGameState().getWorldMap()
    fun getRoom(point: Point) = getGameState().getRoom(point)
    fun getNumberRooms() = getGameState().getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = getGameState().getAmountOfObjectsInRoom(point)
    fun getScore() = getGameState().getScore()

    /**
     * Add object to a room and apply its effects if any exist
     *
     * @param point location to add to
     * @param gameObject object to add
     */
    fun addToRoom(point: Point, gameObject: GameObject) {
        val newWorld = getWorld()
        newWorld.addGameObjectAndEffects(point, gameObject)
        gameState = getGameState().copyThis(world = newWorld)
    }

    /**
     * Remove object from a room and remove its effects where applicable
     *
     * @param point location to remove from
     * @param gameObject object to remove
     */
    fun removeFromRoom(point: Point, gameObject: GameObject) {
        val newWorld = getWorld()
        newWorld.removeGameObject(point, gameObject)
        gameState = getGameState().copyThis(world = newWorld)
    }

    fun getPlayer() = getGameState().getPlayer()
    fun getPlayerState() = getGameState().getPlayerState()
    fun isPlayerAlive() = getGameState().isPlayerAlive()
    fun getPlayerLocation() = getGameState().getPlayerLocation()
    fun getPlayerDirection() = getGameState().getPlayerDirection()
    fun getPlayerInventory() = getGameState().getPlayerInventory()

    /**
     * @param inventoryItem item to check for
     *
     * @return [Boolean] indicating item exists in player's inventory
     */
    fun playerHasItem(inventoryItem: InventoryItem) = getGameState().playerHasItem(inventoryItem)

    /**
     * Add item to players inventory
     *
     * @param inventoryItem item to add
     */
    fun addToPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = getPlayer()
        newPlayer.addToInventory(inventoryItem)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    /**
     * Remove item from player's inventory
     *
     * @param inventoryItem item to remove
     */
    fun removeFromPlayerInventory(inventoryItem: InventoryItem) {
        val newPlayer = getPlayer()
        newPlayer.removeFromInventory(inventoryItem)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setActive(active: Boolean) {
        gameState = getGameState().copyThis(active)
    }

    fun setPlayerAlive(alive: Boolean) {
        val newPlayer = getPlayer()
        newPlayer.setAlive(alive)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    /**
     * Change player's location and assess any harm this causes to the player
     *
     * @param location where to set the player
     */
    fun setPlayerLocation(location: Point) {
        val newPlayer = getPlayer()
        newPlayer.setLocation(location)
        if (getGameObjects(location).any { it.hasFeature(Dangerous())
                        && (it.getFeature(Dangerous()) as Dangerous).killsPlayer(location, getWorld())}) {
            setPlayerAlive(false)
        }
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerDirection(direction: Direction) {
        val newPlayer = getPlayer()
        newPlayer.setFacing(direction)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerInventory(inventory: PlayerInventory) {
        val newPlayer = getPlayer()
        newPlayer.setInventory(inventory)
        gameState = getGameState().copyThis(player = newPlayer)
    }

    fun setPlayerScore(score: Int) {
        val newPlayer = getPlayer()
        newPlayer.setScore(score)
        gameState = getGameState().copyThis(player = newPlayer)
    }
}
