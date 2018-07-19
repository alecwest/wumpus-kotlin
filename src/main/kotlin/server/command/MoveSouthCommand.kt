package server.command

import game.Game
import game.player.PlayerState
import util.south

class MoveSouthCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                location = game.gameState.player.getLocation().south())
    }
}