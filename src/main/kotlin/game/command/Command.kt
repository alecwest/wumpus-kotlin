package game.command

import game.Game
import game.player.PlayerState
import game.world.GameObject
import game.world.GameObjectFeature.*
import game.world.Perception

abstract class Command {
    internal lateinit var game: Game

    abstract fun execute()

    fun setGame(game: Game) {
        this.game = game
    }

    fun createCommandResult(perceptions: ArrayList<Perception> = createPerceptions(),
                            playerState: PlayerState = game.getPlayerState(),
                            gameObjects: ArrayList<GameObject> = game.getGameObjects()): CommandResult {
        return CommandResult(perceptions, playerState, gameObjects)
    }

    fun createPerceptions(): ArrayList<Perception> {
        val location = game.getPlayerLocation()
        val perceptionList = arrayListOf<Perception>()
        for (content in game.getGameObjects(location).filter { it.hasFeature(Perceptable()) }) {
            (content.getFeature(Perceptable()) as Perceptable).perception?.let { perceptionList.add(it) }
        }
        return perceptionList
    }
}