package server.command.move

import game.Game
import server.command.Command
import util.Direction

class MoveCommand(private val direction: Direction): Command {
    override fun execute() {
        when(direction) {
            Direction.NORTH -> MoveNorthCommand().execute()
            Direction.EAST -> MoveEastCommand().execute()
            Direction.SOUTH -> MoveSouthCommand().execute()
            Direction.WEST -> MoveWestCommand().execute()
        }
    }

}