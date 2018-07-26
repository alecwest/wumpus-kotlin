package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

internal class GrabArrowCommand: Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.ARROW)) {
            game.addToPlayerInventory(InventoryItem.ARROW)
            game.removeFromRoom(playerLocation, RoomContent.ARROW)
        }
    }
}