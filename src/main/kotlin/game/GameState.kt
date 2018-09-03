package game

import game.player.InventoryItem
import game.player.Player
import game.world.GameObject
import game.world.World
import java.awt.Point

/**
 * GameState holds the data on the world and the player
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
    fun hasGameObject(point: Point, content: GameObject) = getWorld().hasGameObject(point, content)
    fun roomIsValid(point: Point) = getWorld().roomIsValid(point)
    fun roomIsEmpty(point: Point) = getWorld().roomIsEmpty(point)
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
    fun playerHasItem(inventoryItem: InventoryItem) = getPlayer().hasItem(inventoryItem)

    fun copyThis(active: Boolean = this.active,
                 world: World = this.world,
                 player: Player = this.player) = GameState(active, world, player)
}
