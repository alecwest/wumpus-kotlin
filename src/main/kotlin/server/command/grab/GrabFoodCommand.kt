package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getWorld().hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.getWorld().removeRoomContent(playerLocation, RoomContent.FOOD)
        }
    }

}