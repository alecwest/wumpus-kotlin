package server.command.move

import game.Game
import game.world.RoomContent
import server.command.Command
import util.Direction
import util.adjacent
import java.awt.Point

class MoveCommand(private val game: Game, private val direction: Direction): Command {
    override fun execute() {
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