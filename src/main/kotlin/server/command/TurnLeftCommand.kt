package server.command

import game.Game
import util.left

class TurnLeftCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().left())
    }
}