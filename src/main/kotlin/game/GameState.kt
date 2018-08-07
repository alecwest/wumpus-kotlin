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
    fun getWorldSize() = world.getSize()
    fun getRooms() = world.getRooms()
    fun getGameObjects(point: Point) = world.getGameObject(point)
    fun hasGameObject(point: Point, content: GameObject) = world.hasGameObject(point, content)
    fun roomIsValid(point: Point) = world.roomIsValid(point)
    fun roomIsEmpty(point: Point) = world.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = world.getRoomIndex(point)
    fun getWorldMap() = world.getWorldMap()
    fun getRoom(point: Point) = world.getRoom(point)
    fun getNumberRooms() = world.getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = world.getAmountOfObjectsInRoom(point)

    fun getPlayer() = player
    fun getPlayerState() = player.getPlayerState()
    fun isPlayerAlive() = player.isAlive()
    fun getPlayerLocation() = player.getLocation()
    fun getPlayerDirection() = player.getDirection()
    fun getPlayerInventory() = player.getInventory()
    fun playerHasItem(inventoryItem: InventoryItem) = player.hasItem(inventoryItem)

    fun copyThis(active: Boolean = this.active,
                 world: World = this.world,
                 player: Player = this.player) = GameState(active, world, player)
}
