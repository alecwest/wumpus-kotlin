package game

import game.player.Player
import game.world.World

/**
 * Game facilitates interactions between a world and its player
 */
data class Game(val gameState: GameState = GameState()) {
    fun gameOver() = gameState.gameOver()
}


data class GameState(val active: Boolean = true,
                     val world: World = World(),
                     val player: Player = Player()) {
    fun gameOver() = !active
}