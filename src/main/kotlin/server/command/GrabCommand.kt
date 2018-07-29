package server.command

import game.Game
import game.player.InventoryItem
import game.world.RoomContent

class GrabCommand(private val game: Game, private val inventoryItem: InventoryItem): Command(game) {
    override fun execute() {
        when(inventoryItem){
            InventoryItem.ARROW -> GrabArrowCommand(game).execute()
            InventoryItem.FOOD -> GrabFoodCommand(game).execute()
            InventoryItem.GOLD -> GrabGoldCommand(game).execute()
        }
        game.setCommandResult(CommandResult(createPerceptions()))
    }
}

private class GrabArrowCommand(private val game: Game): Command(game) {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.ARROW)) {
            game.addToPlayerInventory(InventoryItem.ARROW)
            game.removeFromRoom(playerLocation, RoomContent.ARROW)
        }
    }
}

private class GrabFoodCommand(private val game: Game): Command(game) {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.removeFromRoom(playerLocation, RoomContent.FOOD)
        }
    }
}

private class GrabGoldCommand(private val game: Game): Command(game) {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.addToPlayerInventory(InventoryItem.GOLD)
            game.removeFromRoom(playerLocation, RoomContent.GOLD)
        }
    }
}