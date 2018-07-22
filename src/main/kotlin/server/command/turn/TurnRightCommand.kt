package server.command.turn

import game.Game
import server.command.Command
import util.right

internal class TurnRightCommand(override var game: Game): Command {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().right())
    }
}