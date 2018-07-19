package server.command

import game.Game
import util.left

class TurnLeftCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                facing = game.gameState.player.getDirection().left())
    }
}