package server.command

import game.Game
import game.player.PlayerState
import util.north

class MoveNorthCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                location = game.gameState.player.getLocation().north())
    }
}