package server.command

import game.Game
import game.player.Inventory
import game.player.PlayerInventory
import game.world.RoomContent

class GrabFoodCommand(val game: Game): Command {
    override fun execute() {
        val playerLocation = game.gameState.player.getLocation()
        val playerInventory = game.gameState.player.getInventory().toMutableMap()
        playerInventory[Inventory.FOOD] = 1
        if (game.gameState.world.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.gameState.world.removeRoomContent(playerLocation, RoomContent.FOOD)
            game.gameState.player.playerState = game.gameState.player.playerState.copyThis(
                    inventory = PlayerInventory(playerInventory.toMap())
            )
        }
    }

}