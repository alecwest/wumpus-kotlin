package server.command.turn

import game.Game
import server.command.Command
import util.Direction

class TurnCommand(private val game: Game, private val targetDirection: Direction): Command {
    override fun execute() {
        val currentDirection = game.getPlayerDirection()
        when(currentDirection) {
            Direction.NORTH -> if(targetDirection == Direction.EAST) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.EAST -> if(targetDirection == Direction.SOUTH) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.SOUTH -> if(targetDirection == Direction.WEST) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
            Direction.WEST -> if(targetDirection == Direction.NORTH) TurnRightCommand(game).execute() else TurnLeftCommand(game).execute()
        }
    }


}