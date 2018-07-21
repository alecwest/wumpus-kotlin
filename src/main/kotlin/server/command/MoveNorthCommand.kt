package server.command

import game.Game
import util.north

class MoveNorthCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}