package server.command.move

import game.Game
import server.command.Command
import util.north

class MoveNorthCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}