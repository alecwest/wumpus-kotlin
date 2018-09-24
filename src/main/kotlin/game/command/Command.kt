package game.command

import game.Game
import game.player.PlayerState
import game.world.GameObjectFeature.*
import game.world.Perception

/**
 * Base class for implementation of Command pattern
 */
abstract class Command {
    /**
     * null until [setGame] is called
     */
    internal var game: Game? = null

    /**
     * Execute this command
     */
    abstract fun execute()

    /**
     * Get the cost of the command's move
     */
    open fun getMoveCost(playerState: PlayerState? = null): Int {
        return 1
    }

    /**
     * Set the game for the Command
     */
    fun setGame(game: Game) {
        this.game = game
    }
}