package game.world

import util.Direction
import util.toPlayerMapRepresentation

/**
 * Some features of the Room that will differ from the c++ implementation:
 *      Rooms cannot know their coordinates. This knowledge shouldn't be passed down to a Room.
 *      Rooms cannot know the world size as well as the number of rooms in the world.
 *      A room only contains information on what exists inside it.
 *      It is up to the client to determine any "status" or "inferences" about any room.
 */
class Room(private val gameObjects: MutableSet<GameObject> = mutableSetOf()) {
    fun getGameObjects() = gameObjects.toSet()

    fun addGameObject(content: GameObject) {
        if (!hasGameObject(content) && !isFull()) {
            removeAllObjectsIfRoomFilling(content)
            gameObjects.add(content)
        }
    }

    private fun removeAllObjectsIfRoomFilling(content: GameObject) {
        if (content.hasFeature(GameObjectFeature.RoomFilling())) {
            gameObjects.removeIf { true }
        }
    }

    fun removeGameObject(content: GameObject) {
        if (hasGameObject(content)) {
            gameObjects.remove(content)
        }
    }

    fun hasGameObject(content: GameObject): Boolean {
        return gameObjects.contains(content)
    }

    fun getAmountOfObjects(): Int {
        return gameObjects.size
    }

    fun isEmpty(): Boolean {
        return gameObjects.size == 0
    }

    fun isFull(): Boolean {
        return gameObjects.any { it.hasFeature(GameObjectFeature.Blocking()) }
    }

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