package game.command

import game.Game
import game.world.Perception
import game.world.toPerception

abstract class Command(private val game: Game) {
    abstract fun execute()

    fun createPerceptions(): ArrayList<Perception> {
        val location = game.getPlayerLocation()
        val perceptionList = arrayListOf<Perception>()
        for (content in game.getRoomContent(location)) {
            content.toPerception()?.let { perceptionList.add(it) }
        }
        return perceptionList
    }
}