package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabGoldCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.gameState.player.getLocation()
        if (game.gameState.world.hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.gameState.player.addToInventory(InventoryItem.GOLD)
            game.gameState.world.removeRoomContent(playerLocation, RoomContent.GOLD)
        }
    }
}