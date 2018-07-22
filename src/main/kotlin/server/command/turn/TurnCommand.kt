package server.command.turn

import game.Game
import server.command.Command
import util.Direction

class TurnCommand(private val game: Game, private val targetDirection: Direction): Command {
    override fun execute() {
        val currentDirection = game.getPlayerDirection()
        when(targetDirection) {
            Direction.NORTH -> if(currentDirection == Direction.WEST) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.EAST -> if(currentDirection == Direction.SOUTH) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.SOUTH -> if(currentDirection == Direction.EAST) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.WEST -> if(currentDirection == Direction.NORTH) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
        }
    }


}