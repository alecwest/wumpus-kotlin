package server.command

import game.Game
import game.world.Perception
import game.world.RoomContent
import game.world.toPerception
import util.*
import java.awt.Point

class MoveCommand(private val game: Game): Command {
    override fun execute() {
        val direction = game.getPlayerDirection()
        val targetLocation = game.getPlayerLocation().adjacent(direction)
        val perceptionList = arrayListOf<Perception>()
        when {
            canEnterRoom(targetLocation) -> deferExecution(direction)
            game.roomIsValid(targetLocation) -> perceptionList.add(Perception.BUMP)
            else -> perceptionList.add(Perception.WALL)
        }

        perceptionList.addAll(createPerceptions())
        game.setCommandResult(CommandResult(perceptionList))
    }

    private fun createPerceptions(): ArrayList<Perception> {
        val location = game.getPlayerLocation()
        val perceptionList = arrayListOf<Perception>()
        for (content in game.getRoomContent(location)) {
            content.toPerception()?.let { perceptionList.add(it) }
        }
        return perceptionList
    }

    private fun canEnterRoom(point: Point): Boolean {
        if(game.hasRoomContent(point, RoomContent.BLOCKADE) || !game.roomIsValid(point)) {
            return false
        }
        return true
    }

    private fun deferExecution(direction: Direction) {
        when(direction) {
            Direction.NORTH -> MoveNorthCommand(game).execute()
            Direction.EAST -> MoveEastCommand(game).execute()
            Direction.SOUTH -> MoveSouthCommand(game).execute()
            Direction.WEST -> MoveWestCommand(game).execute()
        }
    }
}

private class MoveNorthCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}

private class MoveEastCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().east())
    }
}

class MoveSouthCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().south())
    }
}

class MoveWestCommand(private val game: Game): Command {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}
