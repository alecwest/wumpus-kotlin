package server.command

import game.Game
import game.player.InventoryItem.*

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.addToInventory(FOOD)
    }

}