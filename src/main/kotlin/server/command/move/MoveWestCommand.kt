package server.command.move

import game.Game
import server.command.Command
import util.west

internal class MoveWestCommand: Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}