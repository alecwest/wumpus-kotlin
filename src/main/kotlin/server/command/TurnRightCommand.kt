package server.command

import game.Game
import util.right

class TurnRightCommand(val game: Game): Command {
    override fun execute() {
        game.getGameState().player.setFacing(game.getGameState().player.getDirection().right())
    }
}