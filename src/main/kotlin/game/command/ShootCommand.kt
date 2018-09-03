package game.command

import game.command.CommandResult.Companion.createCommandResult
import game.player.InventoryItem
import game.player.PlayerState
import game.world.Perception
import game.world.GameObject
import game.world.GameObjectFeature.*
import game.world.toGameObject
import util.adjacent
import java.awt.Point

class ShootCommand: Command() {
    private val perceptionList = mutableSetOf<Perception>()

    override fun execute() {
        game?.let { game ->
            if (game.playerHasItem(InventoryItem.ARROW)) {
                var currentRoom = game.getPlayerLocation().adjacent(game.getPlayerDirection())
                game.removeFromPlayerInventory(InventoryItem.ARROW)
                loop@ while (game.roomIsValid(currentRoom)) {
                    val destructables = getDestructablesFromRoom(currentRoom)
                    for (destructable in destructables) {
                        if ((destructable.getFeature(Destructable()) as Destructable).weaknesses.contains(GameObject.ARROW)) {
                            kill(currentRoom, destructable)
                            break@loop
                        }
                    }
                    currentRoom = currentRoom.adjacent(game.getPlayerDirection())
                }
            }

            game.setPlayerScore(game.getScore() + getMoveCost(game.getPlayerState()))
            game.setCommandResult(createCommandResult(game, perceptionList))
        }
    }

    override fun getMoveCost(playerState: PlayerState?): Int {
        return (InventoryItem.ARROW.toGameObject().getFeature(Shootable()) as Shootable).cost
    }

    private fun getDestructablesFromRoom(room: Point): ArrayList<GameObject> {
        val destructables = arrayListOf<GameObject>()
        game?.let { game ->
            destructables.addAll(game.getGameObjects(room).filter {
                it.hasFeature(Destructable())
            } as ArrayList<GameObject>)
        }
        return destructables
    }

    private fun kill(room: Point, gameObject: GameObject) {
        game?.let { game ->
            game.removeFromRoom(room, gameObject)
        perceptionList.add(Perception.SCREAM)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShootCommand) return false

        if (perceptionList != other.perceptionList) return false

        return true
    }

    override fun toString(): String {
        return "ShootCommand(perceptionList=$perceptionList)"
    }
}