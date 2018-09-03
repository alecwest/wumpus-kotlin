package game.command

import game.command.CommandResult.Companion.createCommandResult
import game.player.InventoryItem
import game.player.PlayerState
import game.world.GameObjectFeature.*
import game.world.toGameObject

class GrabCommand(private val inventoryItem: InventoryItem): Command() {
    override fun execute() {
        game?.let { game ->
            val gameObject = inventoryItem.toGameObject()
            val playerLocation = game.getPlayerLocation()

            game.setPlayerScore(game.getScore() + getMoveCost(game.getPlayerState()))
            if (game.hasGameObject(playerLocation, gameObject)) {
                game.addToPlayerInventory(inventoryItem)
                game.removeFromRoom(playerLocation, gameObject)
            }

            game.setCommandResult(createCommandResult(game))
        }
    }

    override fun getMoveCost(playerState: PlayerState?): Int {
        game?.let { game ->
            if (game.hasGameObject(game.getPlayerLocation(), inventoryItem.toGameObject())) {
                return -(inventoryItem.toGameObject().getFeature(Grabbable()) as Grabbable).value +
                        super.getMoveCost(playerState)
            }
        }
        return super.getMoveCost(playerState)
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
