package game.command

import game.player.InventoryItem
import game.world.Perception
import game.world.GameObject
import game.world.GameObjectFeature.*
import util.adjacent
import java.awt.Point

class ShootCommand: Command() {
    private val perceptionList = arrayListOf<Perception>()

    override fun execute() {
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
        game.setCommandResult(createCommandResult(perceptionList))
    }

    private fun getDestructablesFromRoom(room: Point) = game.getGameObjects(room).filter {
        it.hasFeature(Destructable())
    } as ArrayList<GameObject>

    private fun kill(room: Point, gameObject: GameObject) {
        game.removeFromRoom(room, gameObject)
        perceptionList.add(Perception.SCREAM)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShootCommand) return false

        if (perceptionList != other.perceptionList) return false

        return true
    }
}