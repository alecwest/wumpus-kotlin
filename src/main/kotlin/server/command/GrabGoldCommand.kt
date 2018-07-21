package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabGoldCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getGameWorld().hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.addToPlayerInventory(InventoryItem.GOLD)
            game.getGameWorld().removeRoomContent(playerLocation, RoomContent.GOLD)
        }
    }
}