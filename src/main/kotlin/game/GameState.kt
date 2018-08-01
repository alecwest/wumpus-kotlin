package game

import game.player.Player
import game.world.RoomContent
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
    fun getRoomContent(point: Point) = world.getRoomContent(point)
    fun hasRoomContent(point: Point, content: RoomContent) = world.hasRoomContent(point, content)
    fun roomIsValid(point: Point) = world.roomIsValid(point)
    fun roomIsEmpty(point: Point) = world.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = world.getRoomIndex(point)
    fun getWorldMap() = world.getWorldMap()
    fun getRoom(point: Point) = world.getRoom(point)
    fun getNumberRooms() = world.getNumberRooms()
    fun getAmountOfContentInRoom(point: Point) = world.getAmountOfContentInRoom(point)

    fun getPlayer() = player
    fun getPlayerState() = player.getPlayerState()
    fun isPlayerAlive() = player.isAlive()
    fun getPlayerLocation() = player.getLocation()
    fun getPlayerDirection() = player.getDirection()
    fun getPlayerInventory() = player.getInventory()

    fun copyThis(active: Boolean = this.active,
                 world: World = this.world,
                 player: Player = this.player) = GameState(active, world, player)
}
