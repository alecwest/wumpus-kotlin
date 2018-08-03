package game.command

import game.player.InventoryItem
import game.world.Destructable
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
                val destructables = getDestructablesFromRoom(currentRoom)
                for (destructable in destructables) {
                    if (destructable.hasWeakness(InventoryItem.ARROW)) {
                        kill(currentRoom, destructable)
                        break@loop
                    }
                }
                currentRoom = currentRoom.adjacent(game.getPlayerDirection())
            }
        }
        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun getDestructablesFromRoom(room: Point) = game.getRoomContent(room).filter {
        it is Destructable
    } as ArrayList<Destructable>

    private fun kill(room: Point, roomContent: RoomContent) {
        game.removeFromRoom(room, roomContent)
        perceptionList.add(Perception.SCREAM)
    }
}