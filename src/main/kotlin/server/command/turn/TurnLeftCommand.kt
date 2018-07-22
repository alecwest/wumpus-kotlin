package server.command.turn

import game.Game
import server.command.Command
import util.left

internal class TurnLeftCommand(override var game: Game): Command {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().left())
    }
}