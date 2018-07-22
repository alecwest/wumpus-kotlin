package server.command.move

import game.Game
import server.command.Command
import util.north

internal class MoveNorthCommand(override var game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}