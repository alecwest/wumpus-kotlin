package game.command

import util.Direction
import util.left
import util.right

class TurnCommand(private val targetDirection: Direction): Command() {
    private var startingDirection: Direction? = null // For record keeping

    override fun execute() {
        startingDirection = game.getPlayerDirection()
        var currentDirection = startingDirection!!
        while (currentDirection != targetDirection) {
            val command = when(currentDirection) {
                Direction.NORTH -> if(targetDirection == Direction.EAST) TurnRightCommand() else TurnLeftCommand()
                Direction.EAST -> if(targetDirection == Direction.SOUTH) TurnRightCommand() else TurnLeftCommand()
                Direction.SOUTH -> if(targetDirection == Direction.WEST) TurnRightCommand() else TurnLeftCommand()
                Direction.WEST -> if(targetDirection == Direction.NORTH) TurnRightCommand() else TurnLeftCommand()
            }
            command.setGame(this.game)
            command.execute()
            game.setCommandResult(createCommandResult())
            currentDirection = game.getPlayerDirection()
        }
    }

    override fun getMoveCost(): Int {
        val startingDirection = startingDirection ?: game.getPlayerDirection()
        return when (targetDirection) {
            startingDirection.right().right() -> 2
            startingDirection -> 0
            else -> 1
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TurnCommand) return false

        if (targetDirection != other.targetDirection) return false

        return true
    }

    override fun toString(): String {
        return "TurnCommand(targetDirection=$targetDirection)"
    }
}

private class TurnLeftCommand: Command() {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().left())
    }
}

private class TurnRightCommand: Command() {
    override fun execute() {
        game.setPlayerDirection(game.getPlayerDirection().right())
    }
}