package server.command.grab

import game.Game
import game.player.InventoryItem
import server.command.Command

class GrabCommand(private val inventoryItem: InventoryItem): Command {
    override fun execute() {
        when(inventoryItem){
            InventoryItem.ARROW -> GrabArrowCommand().execute()
            InventoryItem.FOOD -> GrabFoodCommand().execute()
            InventoryItem.GOLD -> GrabGoldCommand().execute()
        }
    }

}