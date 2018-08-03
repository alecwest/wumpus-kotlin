package game.command

import game.player.InventoryItem
import game.world.Perception
import game.world.RoomContent
import util.adjacent

class ShootCommand: Command() {
    override fun execute() {
        val perceptionList = arrayListOf<Perception>()
        if (game.playerHasItem(InventoryItem.ARROW)) {
            var currentRoom = game.getPlayerLocation().adjacent(game.getPlayerDirection())
            game.removeFromPlayerInventory(InventoryItem.ARROW)
            while (game.roomIsValid(currentRoom)) {
                val roomContent = game.getRoomContent(currentRoom)
                if (roomContent.contains(RoomContent.SUPMUW) ||
                        roomContent.contains(RoomContent.SUPMUW_EVIL) ||
                        roomContent.contains(RoomContent.WUMPUS)) {
                    game.removeFromRoom(currentRoom, RoomContent.SUPMUW)
                    game.removeFromRoom(currentRoom, RoomContent.WUMPUS)
                    game.removeFromRoom(currentRoom, RoomContent.SUPMUW_EVIL)
                    perceptionList.add(Perception.SCREAM)
                    break
                }
                currentRoom = currentRoom.adjacent(game.getPlayerDirection())
            }
        }
        game.setCommandResult(createCommandResult(perceptionList))
    }
}