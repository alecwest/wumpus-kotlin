package game.command

import game.Game
import game.world.Perception
import game.world.RoomContent
import util.*
import java.awt.Point

class MoveCommand(private val game: Game): Command(game) {
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
        game.setCommandResult(CommandResult(perceptionList))
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

private class MoveNorthCommand(private val game: Game): Command(game) {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().north())
    }
}

private class MoveEastCommand(private val game: Game): Command(game) {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().east())
    }
}

class MoveSouthCommand(private val game: Game): Command(game) {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().south())
    }
}

class MoveWestCommand(private val game: Game): Command(game) {
    override fun execute() {
        game.setPlayerLocation(game.getPlayerLocation().west())
    }
}
