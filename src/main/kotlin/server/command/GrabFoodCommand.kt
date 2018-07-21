package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getGameWorld().hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.getGameWorld().removeRoomContent(playerLocation, RoomContent.FOOD)
        }
    }

}