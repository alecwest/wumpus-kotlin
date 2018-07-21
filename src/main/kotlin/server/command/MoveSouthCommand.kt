package server.command

import game.Game
import util.south

class MoveSouthCommand(val game: Game): Command {
    override fun execute() {
        game.getGameState().player.setLocation(game.getGameState().player.getLocation().south())
    }
}