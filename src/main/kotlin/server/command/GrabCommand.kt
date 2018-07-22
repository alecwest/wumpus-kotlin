package server.command

import game.Game
import game.player.InventoryItem

class GrabCommand(private val game: Game, private val inventoryItem: InventoryItem): Command {
    override fun execute() {
        when(inventoryItem){
            InventoryItem.ARROW -> GrabArrowCommand(game).execute()
            InventoryItem.FOOD -> GrabFoodCommand(game).execute()
            InventoryItem.GOLD -> GrabGoldCommand(game).execute()
        }
    }

}