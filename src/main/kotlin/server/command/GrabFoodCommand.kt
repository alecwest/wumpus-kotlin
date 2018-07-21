package server.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.gameState.player.getLocation()
        if (game.gameState.world.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            val playerInventory = game.gameState.player.getInventory().plus(Pair(InventoryItem.FOOD, 1))
            game.gameState.world.removeRoomContent(playerLocation, RoomContent.FOOD)
            game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                    inventory = PlayerInventory(playerInventory.toMap())
            )
        }
    }

}