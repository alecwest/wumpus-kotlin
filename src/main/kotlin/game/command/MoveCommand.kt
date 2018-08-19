package game.command

import game.world.Perception
import game.world.GameObjectFeature.*
import util.*
import java.awt.Point

class MoveCommand: Command() {
    override fun execute() {
        val direction = game.getPlayerDirection()
        val targetLocation = game.getPlayerLocation().adjacent(direction)
        val perceptionList = arrayListOf<Perception>()
        when {
            canEnterRoom(targetLocation) -> {
                deferExecution(direction)
                for (gameObject in game.getGameObjects(targetLocation).filter { it.hasFeature(Perceptable()) }) {
                    val perceptable = (gameObject.getFeature(Perceptable()) as Perceptable).perception ?: continue
                    perceptionList.add(perceptable)
                }
            }
            game.roomIsValid(targetLocation) -> {
                for (gameObject in game.getGameObjects(targetLocation).filter { it.hasFeature(Blocking()) }) {
                    val perception = (gameObject.getFeature(Perceptable()) as Perceptable).perception
                    if (perception != null) perceptionList.add(perception)
                }
            }
            else -> perceptionList.add(Perception.WALL_BUMP)
        }

        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun canEnterRoom(point: Point): Boolean {
        if(game.getGameObjects(point).any { it.hasFeature(Blocking()) } || !game.roomIsValid(point)) {
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

    override fun toString(): String {
        return "MoveCommand()"
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
