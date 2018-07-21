package server.command

import game.Game
import util.west

class MoveWestCommand(val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}