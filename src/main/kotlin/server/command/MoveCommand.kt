package server.command

import game.Game
import game.world.RoomContent
import util.*
import java.awt.Point

class MoveCommand(private val game: Game): Command {
    override fun execute() {
        val direction = game.getPlayerDirection()
        if (canEnterRoom(game.getPlayerLocation().adjacent(direction))) {
            when(direction) {
                Direction.NORTH -> MoveNorthCommand(game).execute()
                Direction.EAST -> MoveEastCommand(game).execute()
                Direction.SOUTH -> MoveSouthCommand(game).execute()
                Direction.WEST -> MoveWestCommand(game).execute()
            }
        }
    }

    private fun canEnterRoom(point: Point): Boolean {
        if(game.hasRoomContent(point, RoomContent.BLOCKADE) || !game.roomIsValid(point)) {
            return false
        }
        return true
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
