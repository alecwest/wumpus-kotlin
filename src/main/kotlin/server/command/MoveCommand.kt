package server.command

import game.Game
import util.Direction

class MoveCommand(private val game: Game, private val direction: Direction): Command {
    override fun execute() {
        when(direction) {
            Direction.NORTH -> MoveNorthCommand(game).execute()
            Direction.EAST -> MoveEastCommand(game).execute()
            Direction.SOUTH -> MoveSouthCommand(game).execute()
            Direction.WEST -> MoveWestCommand(game).execute()
        }
    }

}