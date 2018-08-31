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

    fun createCommandResult(perceptions: Set<Perception> = createPerceptions(),
                            playerState: PlayerState = game?.getPlayerState() ?: PlayerState(),
                            gameActive: Boolean = game?.getActive() ?: true): CommandResult {
        return CommandResult(perceptions, playerState, gameActive)
    }

    fun createPerceptions(): Set<Perception> {
        val perceptionList = mutableSetOf<Perception>()
        if (game != null) {
            val location = game!!.getPlayerLocation()
            for (content in game!!.getGameObjects(location).filter { it.hasFeature(Perceptable()) }) {
                (content.getFeature(Perceptable()) as Perceptable).perception?.let { perceptionList.add(it) }
            }
        }
        return perceptionList
    }
}