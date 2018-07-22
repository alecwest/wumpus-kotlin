package server.command.move

import game.Game
import server.command.Command
import util.east

internal class MoveEastCommand(override var game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().east())
    }
}