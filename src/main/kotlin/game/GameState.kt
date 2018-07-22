package game

import game.player.Player
import game.world.World

/**
 * GameState holds the data on the world and the player
 */
data class GameState(private val active: Boolean = true,
                     private val world: World = World(),
                     private val player: Player = Player()) {
    fun getActive() = active
    fun gameOver() = !active

    fun getWorld() = world

    fun getPlayer() = player
    fun isPlayerAlive() = player.isAlive()
    fun getPlayerLocation() = player.getLocation()
    fun getPlayerDirection() = player.getDirection()
    fun getPlayerInventory() = player.getInventory()

    fun copyThis(active: Boolean = this.active,
                 world: World = this.world,
                 player: Player = this.player) = GameState(active, world, player)
}
