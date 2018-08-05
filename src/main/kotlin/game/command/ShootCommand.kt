package game.command

import game.player.InventoryItem
import game.world.Dangerous
import game.world.Perception
import game.world.RoomContent
import util.adjacent
import java.awt.Point

class ShootCommand: Command() {
    private val perceptionList = arrayListOf<Perception>()

    override fun execute() {
        if (game.playerHasItem(InventoryItem.ARROW)) {
            var currentRoom = game.getPlayerLocation().adjacent(game.getPlayerDirection())
            game.removeFromPlayerInventory(InventoryItem.ARROW)
            loop@ while (game.roomIsValid(currentRoom)) {
                val dangers = getDangersFromRoom(currentRoom)
                for (dangers in dangers) {
                    if (dangers.hasWeakness(InventoryItem.ARROW)) {
                        kill(currentRoom, dangers)
                        break@loop
                    }
                }
                currentRoom = currentRoom.adjacent(game.getPlayerDirection())
            }
        }
        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun getDangersFromRoom(room: Point) = game.getRoomContent(room).filter {
        it is Dangerous
    } as ArrayList<Dangerous>

    private fun kill(room: Point, roomContent: RoomContent) {
        game.removeFromRoom(room, roomContent)
        perceptionList.add(Perception.SCREAM)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShootCommand) return false

        if (perceptionList != other.perceptionList) return false

        return true
    }
}