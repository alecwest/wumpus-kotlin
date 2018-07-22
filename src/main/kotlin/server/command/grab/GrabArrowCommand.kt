package server.command.grab

import game.Game
import game.player.InventoryItem
import game.world.RoomContent
import server.command.Command

internal class GrabArrowCommand(override var game: Game): Command {
    override fun execute() {
        //TODO clean this up (and the turn commands)
        val playerLocation = game.getPlayerLocation()
        if (game.getWorld().hasRoomContent(playerLocation, RoomContent.ARROW)) {
            game.addToPlayerInventory(InventoryItem.ARROW)
            game.getWorld().removeRoomContent(playerLocation, RoomContent.ARROW)
        }
    }
}