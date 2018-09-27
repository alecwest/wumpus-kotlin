package game

import game.player.InventoryItem
import game.player.Player
import game.world.GameObject
import game.world.World
import java.awt.Point

/**
 * GameState holds the data on the world and the player
 *
 * @param active the current state of the game
 * @param world the world
 */
data class GameState(private val active: Boolean = true,
                     private val world: World = World(),
                     private val player: Player = Player()) {
    fun getActive() = active
    fun gameOver() = !active

    fun getWorld() = world
    fun getWorldSize() = getWorld().getSize()
    fun getRooms() = getWorld().getRooms()
    fun getGameObjects(point: Point) = getWorld().getGameObjects(point)

    /**
     * @param point room to check
     * @param content object to check for
     *
     * @return [Boolean] indicating object exists in room
     */
    fun hasGameObject(point: Point, content: GameObject) = getWorld().hasGameObject(point, content)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room is within world boundaries
     */
    fun roomIsValid(point: Point) = getWorld().roomIsValid(point)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room has nothing in it
     */
    fun roomIsEmpty(point: Point) = getWorld().roomIsEmpty(point)

    /**
     * @param point room to get
     *
     * @return [Int] index or room in [World]s array of rooms
     */
    fun getRoomIndex(point: Point) = getWorld().getRoomIndex(point)
    fun getWorldMap() = getWorld().getWorldMap()
    fun getRoom(point: Point) = getWorld().getRoom(point)
    fun getNumberRooms() = getWorld().getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = getWorld().getAmountOfObjectsInRoom(point)

    fun getPlayer() = player
    fun getPlayerState() = getPlayer().getPlayerState()
    fun isPlayerAlive() = getPlayer().isAlive()
    fun getPlayerLocation() = getPlayer().getLocation()
    fun getPlayerDirection() = getPlayer().getDirection()
    fun getPlayerInventory() = getPlayer().getInventory()
    fun getScore() = getPlayer().getScore()

    /**
     * @param inventoryItem item to check for
     *
     * @return [Boolean] indicating item exists in player's inventory
     */
    fun playerHasItem(inventoryItem: InventoryItem) = getPlayer().hasItem(inventoryItem)

    /**
     * Deep copy the [GameState]
     *
     * @param active
     * @param world
     * @param player
     *
     * @return [GameState] copy with passed values modified
     */
    fun copyThis(active: Boolean = this.active,
                 world: World = this.world,
                 player: Player = this.player) = GameState(active, world, player)
}
