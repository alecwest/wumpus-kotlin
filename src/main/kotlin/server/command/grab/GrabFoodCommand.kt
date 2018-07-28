package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

internal class GrabFoodCommand(private val game: Game): Command {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.removeFromRoom(playerLocation, RoomContent.FOOD)
        }
    }

}