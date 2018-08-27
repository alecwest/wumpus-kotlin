package game.command

import game.player.InventoryItem
import game.world.GameObject

class GrabCommand(private val inventoryItem: InventoryItem): Command() {
    override fun execute() {
        game?.let { game ->
            var command = when(inventoryItem){
                InventoryItem.ARROW -> GrabArrowCommand()
                InventoryItem.FOOD -> GrabFoodCommand()
                InventoryItem.GOLD -> GrabGoldCommand()
            }

            command.setGame(game)
            command.execute()
            game.setCommandResult(createCommandResult())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GrabCommand) return false

        if (inventoryItem != other.inventoryItem) return false

        return true
    }

    override fun toString(): String {
        return "GrabCommand(inventoryItem=$inventoryItem)"
    }
}

private class GrabArrowCommand: Command() {
    override fun execute() {
        val playerLocation = game!!.getPlayerLocation()
        if (game!!.hasGameObject(playerLocation, GameObject.ARROW)) {
            game!!.addToPlayerInventory(InventoryItem.ARROW)
            game!!.removeFromRoom(playerLocation, GameObject.ARROW)
        }
    }
}

private class GrabFoodCommand: Command() {
    override fun execute() {
        val playerLocation = game!!.getPlayerLocation()
        if (game!!.hasGameObject(playerLocation, GameObject.FOOD)) {
            game!!.addToPlayerInventory(InventoryItem.FOOD)
            game!!.removeFromRoom(playerLocation, GameObject.FOOD)
        }
    }
}

private class GrabGoldCommand: Command() {
    override fun execute() {
        val playerLocation = game!!.getPlayerLocation()
        if (game!!.hasGameObject(playerLocation, GameObject.GOLD)) {
            game!!.addToPlayerInventory(InventoryItem.GOLD)
            game!!.removeFromRoom(playerLocation, GameObject.GOLD)
        }
    }
}