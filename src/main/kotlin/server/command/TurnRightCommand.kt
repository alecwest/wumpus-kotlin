package server.command

import game.Game
import util.right

class TurnRightCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.setFacing(game.gameState.player.getDirection().right())
    }
}