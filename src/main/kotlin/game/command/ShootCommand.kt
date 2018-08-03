package game.command

import game.player.InventoryItem
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
                val roomContent = game.getRoomContent(currentRoom)

                when {
                    roomContent.contains(RoomContent.SUPMUW) -> {
                        kill(currentRoom, RoomContent.SUPMUW)
                        break@loop
                    }
                    roomContent.contains(RoomContent.SUPMUW_EVIL) -> {
                        kill(currentRoom, RoomContent.SUPMUW_EVIL)
                        break@loop
                    }
                    roomContent.contains(RoomContent.WUMPUS) -> {
                        kill(currentRoom, RoomContent.WUMPUS)
                        break@loop
                    }
                }
                currentRoom = currentRoom.adjacent(game.getPlayerDirection())
            }
        }
        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun kill(room: Point, roomContent: RoomContent) {
        game.removeFromRoom(room, roomContent)
        perceptionList.add(Perception.SCREAM)
    }
}