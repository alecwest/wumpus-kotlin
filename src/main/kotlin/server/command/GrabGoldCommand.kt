package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabGoldCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getGameState().player.getLocation()
        if (game.getGameState().world.hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.getGameState().player.addToInventory(InventoryItem.GOLD)
            game.getGameState().world.removeRoomContent(playerLocation, RoomContent.GOLD)
        }
    }
}