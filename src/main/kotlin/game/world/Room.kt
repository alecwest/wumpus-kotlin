package game.world

import util.Direction
import util.toPlayerMapRepresentation

/**
 * Acts as a single cell in the world, containing a set of unique [GameObject]s
 */
class Room(private val gameObjects: MutableSet<GameObject> = mutableSetOf()) {
    /**
     * @return immutable set of [GameObject]s
     */
    fun getGameObjects() = gameObjects.toSet()

    /**
     * Add object to room. If it is [RoomFilling][GameObjectFeature.RoomFilling], it will replace all other objects
     *
     * @param content object to add
     */
    fun addGameObject(content: GameObject) {
        if (!hasGameObject(content) && !isFull()) {
            removeAllObjectsIfRoomFilling(content)
            gameObjects.add(content)
        }
    }

    /**
     * Remove all objects currently in the room if the one given is [RoomFilling][GameObjectFeature.RoomFilling]
     *
     * @param content object to check against
     */
    private fun removeAllObjectsIfRoomFilling(content: GameObject) {
        if (content.hasFeature(GameObjectFeature.RoomFilling())) {
            gameObjects.removeIf { true }
        }
    }

    /**
     * @param content object to remove
     */
    fun removeGameObject(content: GameObject) {
        if (hasGameObject(content)) {
            gameObjects.remove(content)
        }
    }

    /**
     * @param content object to check for
     *
     * @return [Boolean] room has given object
     */
    fun hasGameObject(content: GameObject): Boolean {
        return gameObjects.contains(content)
    }

    fun getAmountOfObjects(): Int {
        return gameObjects.size
    }

    /**
     * @return [Boolean] no objects in the room
     */
    fun isEmpty(): Boolean {
        return gameObjects.size == 0
    }

    /**
     * @return [Boolean] indicating whether or not something in the room is [Blocking][GameObjectFeature.Blocking]
     */
    fun isFull(): Boolean {
        return gameObjects.any { it.hasFeature(GameObjectFeature.Blocking()) }
    }

    /**
     * Get string representation of this room for displaying as a 2D square
     *
     * @param playerDirection direction the player is facing, or null if the player isn't in the room
     *
     * @return [String] representing all Mappable objects (and player, where applicable) in the room
     */
    fun getSmallRoomString(playerDirection: Direction? = null): String {
        var roomString = " ------- \n|x x x x|\n|x x x x|\n|x x x x|\n ------- "
        for (content in gameObjects.filter { it.hasFeature(GameObjectFeature.Mappable()) }) {
            roomString = roomString.replaceFirst(
                    "x", (content.getFeature(
                    GameObjectFeature.Mappable()) as GameObjectFeature.Mappable).character)
        }
        if (playerDirection != null) {
            roomString = roomString.replaceFirst("x", playerDirection.toPlayerMapRepresentation())
        }
        return roomString.replace("x", " ")
    }
}