package server.command.turn

import game.Game
import server.command.Command
import util.Direction

class TurnCommand(private val targetDirection: Direction): Command {
    override fun execute() {
        val currentDirection = game.getPlayerDirection()
        when(targetDirection) {
            Direction.NORTH -> if(currentDirection == Direction.WEST) TurnRightCommand().execute() else TurnLeftCommand().execute()
            Direction.EAST -> if(currentDirection == Direction.SOUTH) TurnRightCommand().execute() else TurnLeftCommand().execute()
            Direction.SOUTH -> if(currentDirection == Direction.EAST) TurnRightCommand().execute() else TurnLeftCommand().execute()
            Direction.WEST -> if(currentDirection == Direction.NORTH) TurnRightCommand().execute() else TurnLeftCommand().execute()
        }
    }


}