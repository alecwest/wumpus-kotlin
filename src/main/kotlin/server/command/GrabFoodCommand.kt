package server.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.addToInventory(InventoryItem.FOOD)
    }

}