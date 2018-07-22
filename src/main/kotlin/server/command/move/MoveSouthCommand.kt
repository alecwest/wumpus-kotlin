package server.command.move

import game.Game
import server.command.Command
import util.south

class MoveSouthCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().south())
    }
}