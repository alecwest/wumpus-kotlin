package server.command

import game.Game
import util.left

class TurnLeftCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.setFacing(game.gameState.player.getDirection().left())
    }
}