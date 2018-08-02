package game.command

import game.Game
import game.player.PlayerState
import game.world.Perception
import game.world.RoomContent
import game.world.toPerception

abstract class Command {
    internal lateinit var game: Game

    abstract fun execute()

    fun setGame(game: Game) {
        this.game = game
    }

    fun createCommandResult(perceptions: ArrayList<Perception> = createPerceptions(),
                            playerState: PlayerState = game.getPlayerState(),
                            roomContent: ArrayList<RoomContent> = game.getRoomContent()): CommandResult {
        return CommandResult(perceptions, playerState, roomContent)
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