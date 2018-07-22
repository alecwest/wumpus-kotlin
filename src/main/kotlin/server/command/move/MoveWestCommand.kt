package server.command.move

import game.Game
import server.command.Command
import util.west

class MoveWestCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}