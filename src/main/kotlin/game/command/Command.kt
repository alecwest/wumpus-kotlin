package game.command

import game.Game
import game.world.Perception
import game.world.toPerception

abstract class Command {
    internal lateinit var game: Game

    abstract fun execute()

    fun setGame(game: Game) {
        this.game = game
    }

    fun createPerceptions(): ArrayList<Perception> {
        val location = game.getPlayerLocation()
        val perceptionList = arrayListOf<Perception>()
        for (content in game.getRoomContent(location)) {
            content.toPerception()?.let { perceptionList.add(it) }
        }
        return perceptionList
    }
}