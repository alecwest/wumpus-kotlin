package game

import game.player.Player
import game.player.PlayerInventory
import game.world.World
import util.Direction
import java.awt.Point

/**
 * Game facilitates interactions between a world and its player
 */
data class Game(private val gameState: GameState = GameState()) {
    fun getGameState() = gameState
    fun gameOver() = gameState.gameOver()
}


data class GameState(val active: Boolean = true,
                     val world: World = World(),
                     val player: Player = Player()) {
    fun gameOver() = !active

    fun setPlayerAlive(alive: Boolean) {
        player.setAlive(alive)
    }

    fun setPlayerLocation(location: Point) {
        player.setLocation(location)
    }

    fun setPlayerFacing(direction: Direction) {
        player.setFacing(direction)
    }

    fun setPlayerInventory(inventory: PlayerInventory) {
        player.setInventory(inventory)
    }
}