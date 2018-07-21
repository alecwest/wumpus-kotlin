package server.command

import game.Game
import util.north

class MoveNorthCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.setLocation(game.gameState.player.getLocation().north())
    }
}