package server.command

import game.Game
import util.east

class MoveEastCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.setLocation(game.gameState.player.getLocation().east())
    }
}