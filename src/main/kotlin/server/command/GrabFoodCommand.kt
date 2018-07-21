package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.gameState.player.getLocation()
        if (game.gameState.world.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.gameState.player.addToInventory(InventoryItem.FOOD)
            game.gameState.world.removeRoomContent(playerLocation, RoomContent.FOOD)
        }
    }

}