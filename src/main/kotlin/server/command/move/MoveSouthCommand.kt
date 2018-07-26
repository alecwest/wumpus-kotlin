package server.command.move

import game.Game
import server.command.Command
import util.south

internal class MoveSouthCommand: Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().south())
    }
}