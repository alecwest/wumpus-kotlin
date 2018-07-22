package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

internal class GrabGoldCommand(override var game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.addToPlayerInventory(InventoryItem.GOLD)
            game.removeFromRoom(playerLocation, RoomContent.GOLD)
        }
    }
}