package game.command

import game.world.Perception
import game.world.RoomContent
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

        perceptionList.addAll(createPerceptions())
        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun canEnterRoom(point: Point): Boolean {
        if(game.hasRoomContent(point, RoomContent.BLOCKADE) || !game.roomIsValid(point)) {
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
