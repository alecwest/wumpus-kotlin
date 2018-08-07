package game.command

import game.world.Perception
import game.world.GameObject
import util.*
import java.awt.Point

class MoveCommand: Command() {
    override fun execute() {
        val direction = game.getPlayerDirection()
        val targetLocation = game.getPlayerLocation().adjacent(direction)
        val perceptionList = arrayListOf<Perception>()
        when {
            canEnterRoom(targetLocation) -> deferExecution(direction)
            game.roomIsValid(targetLocation) -> perceptionList.add(Perception.BLOCKADE_BUMP)
            else -> perceptionList.add(Perception.WALL_BUMP)
        }

        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun canEnterRoom(point: Point): Boolean {
        if(game.hasGameObject(point, GameObject.BLOCKADE) || !game.roomIsValid(point)) {
            return false
        }
        return true
    }

    private fun deferExecution(direction: Direction) {
        val command = when(direction) {
            Direction.NORTH -> MoveNorthCommand()
            Direction.EAST -> MoveEastCommand()
            Direction.SOUTH -> MoveSouthCommand()
            Direction.WEST -> MoveWestCommand()
        }
        command.setGame(this.game)
        command.execute()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MoveCommand) return false
        return true
    }
}

private class MoveNorthCommand: Command() {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}

private class MoveEastCommand: Command() {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().east())
    }
}

class MoveSouthCommand: Command() {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().south())
    }
}

class MoveWestCommand: Command() {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}
