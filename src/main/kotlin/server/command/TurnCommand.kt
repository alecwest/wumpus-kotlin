package server.command

import game.Game
import util.Direction
import util.left
import util.right

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

private class TurnLeftCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().left())
    }
}

private class TurnRightCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().right())
    }
}