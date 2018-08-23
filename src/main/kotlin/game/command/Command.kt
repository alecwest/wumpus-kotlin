package game.command

import game.Game
import game.player.PlayerState
import game.world.GameObject
import game.world.GameObjectFeature.*
import game.world.Perception

abstract class Command {
    internal lateinit var game: Game

    abstract fun execute()

    open fun getMoveCost(): Int {
        return 1
    }

    fun setGame(game: Game) {
        this.game = game
    }

    fun createCommandResult(perceptions: Set<Perception> = createPerceptions(),
                            playerState: PlayerState = game.getPlayerState()): CommandResult {
        return CommandResult(perceptions, playerState)
    }

    fun createPerceptions(): Set<Perception> {
        val location = game.getPlayerLocation()
        val perceptionList = mutableSetOf<Perception>()
        for (content in game.getGameObjects(location).filter { it.hasFeature(Perceptable()) }) {
            (content.getFeature(Perceptable()) as Perceptable).perception?.let { perceptionList.add(it) }
        }
        return perceptionList
    }
}