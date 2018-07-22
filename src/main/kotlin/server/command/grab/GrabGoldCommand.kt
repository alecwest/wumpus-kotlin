package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

internal class GrabGoldCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getWorld().hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.addToPlayerInventory(InventoryItem.GOLD)
            game.getWorld().removeRoomContent(playerLocation, RoomContent.GOLD)
        }
    }
}