package server.command.move

import game.Game
import server.command.Command
import util.Direction

class MoveCommand(override var game: Game, private val direction: Direction): Command {
    override fun execute() {
        when(direction) {
            Direction.NORTH -> MoveNorthCommand(game).execute()
            Direction.EAST -> MoveEastCommand(game).execute()
            Direction.SOUTH -> MoveSouthCommand(game).execute()
            Direction.WEST -> MoveWestCommand(game).execute()
        }
    }

}