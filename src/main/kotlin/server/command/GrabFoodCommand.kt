package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getGameState().player.getLocation()
        if (game.getGameState().world.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.getGameState().player.addToInventory(InventoryItem.FOOD)
            game.getGameState().world.removeRoomContent(playerLocation, RoomContent.FOOD)
        }
    }

}