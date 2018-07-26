package server.command.move

import game.Game
import server.command.Command
import util.east

internal class MoveEastCommand: Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().east())
    }
}