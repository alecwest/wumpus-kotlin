package server.command

import game.Game
import game.player.PlayerState
import util.east

class MoveEastCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                location = game.gameState.player.getLocation().east())
    }
}