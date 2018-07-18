package server.command

import game.Game
import game.player.PlayerState
import util.*

class MoveCommand(val game: Game): Command {
    override fun execute() {
        // TODO make specific move north, south, east, west commands?
        game.gameState.player.playerState = when(game.gameState.player.getDirection()) {
            Direction.NORTH -> PlayerState(location = game.gameState.player.getLocation().north())
            Direction.EAST -> PlayerState(location = game.gameState.player.getLocation().east())
            Direction.SOUTH -> PlayerState(location = game.gameState.player.getLocation().south())
            Direction.WEST -> PlayerState(location = game.gameState.player.getLocation().west())
        }
    }
}