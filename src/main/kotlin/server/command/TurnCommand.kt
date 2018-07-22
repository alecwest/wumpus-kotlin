package server.command

import game.Game
import util.Direction

class TurnCommand(val game: Game, val targetDirection: Direction): Command {
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