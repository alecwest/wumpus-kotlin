package game.command

import game.Game
import game.player.PlayerState
import game.world.GameObjectFeature.*
import game.world.Perception

abstract class Command {
    internal var game: Game? = null

    abstract fun execute()

    open fun getMoveCost(playerState: PlayerState? = null): Int {
        return 1
    }

    fun setGame(game: Game) {
        this.game = game
    }
}