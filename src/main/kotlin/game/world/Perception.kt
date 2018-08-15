package game.world

enum class Perception {
    BLOCKADE_BUMP,
    BREEZE,
    FOOD,
    GLITTER,
    MOO,
    SCREAM,
    STENCH,
    WALL_BUMP
}

fun toGameObjects(perceptions: Set<Perception>): Set<GameObject> {
    val gameObjects = mutableSetOf<GameObject>()
    perceptions.forEach { perception ->
        val gameObjectToMatch = perception.toGameObject() ?: return@forEach
        gameObjects.add(gameObjectToMatch)
    }
    return gameObjects.toSet()
}
