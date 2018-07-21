package server.command

import game.Game
import util.north

class MoveNorthCommand(val game: Game): Command {
    override fun execute() {
        game.getGameState().player.setLocation(game.getGameState().player.getLocation().north())
    }
}