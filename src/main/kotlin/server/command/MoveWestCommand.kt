package server.command

import game.Game
import game.player.PlayerState
import util.west

class MoveWestCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.playerState = PlayerState(location = game.gameState.player.getLocation().west())
    }
}