package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabArrowCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getWorld().hasRoomContent(playerLocation, RoomContent.ARROW)) {
            game.addToPlayerInventory(InventoryItem.ARROW)
            game.getWorld().removeRoomContent(playerLocation, RoomContent.ARROW)
        }
    }
}