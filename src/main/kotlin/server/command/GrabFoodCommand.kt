package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.getWorld().hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.getWorld().removeRoomContent(playerLocation, RoomContent.FOOD)
        }
    }

}