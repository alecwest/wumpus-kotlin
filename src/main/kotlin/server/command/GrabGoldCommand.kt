package server.command

import game.Game
import game.player.InventoryItem.*

class GrabGoldCommand(val game: Game): Command {
    override fun execute() {
        game.gameState.player.addToInventory(GOLD)
    }
}