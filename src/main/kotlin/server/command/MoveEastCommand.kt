package server.command

import game.Game
import util.east

class MoveEastCommand(val game: Game): Command {
    override fun execute() {
        game.getGameState().player.setLocation(game.getGameState().player.getLocation().east())
    }
}