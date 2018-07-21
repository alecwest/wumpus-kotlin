package server.command

import game.Game
import util.south

class MoveSouthCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.setLocation(game.gameState.player.getLocation().south())
    }
}