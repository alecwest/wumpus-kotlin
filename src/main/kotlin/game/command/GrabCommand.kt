package game.command

import game.player.InventoryItem
import game.world.RoomContent

class GrabCommand(private val inventoryItem: InventoryItem): Command() {
    override fun execute() {
        var command = when(inventoryItem){
            InventoryItem.ARROW -> GrabArrowCommand()
            InventoryItem.FOOD -> GrabFoodCommand()
            InventoryItem.GOLD -> GrabGoldCommand()
        }
        command.setGame(this.game)
        command.execute()
        game.setCommandResult(createCommandResult())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GrabCommand) return false

        if (inventoryItem != other.inventoryItem) return false

        return true
    }
}

private class GrabArrowCommand: Command() {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.ARROW)) {
            game.addToPlayerInventory(InventoryItem.ARROW)
            game.removeFromRoom(playerLocation, RoomContent.ARROW)
        }
    }
}

private class GrabFoodCommand: Command() {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.FOOD)) {
            game.addToPlayerInventory(InventoryItem.FOOD)
            game.removeFromRoom(playerLocation, RoomContent.FOOD)
        }
    }
}

private class GrabGoldCommand: Command() {
    override fun execute() {
        val playerLocation = game.getPlayerLocation()
        if (game.hasRoomContent(playerLocation, RoomContent.GOLD)) {
            game.addToPlayerInventory(InventoryItem.GOLD)
            game.removeFromRoom(playerLocation, RoomContent.GOLD)
        }
    }
}