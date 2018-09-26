package game.world

/**
 * An enumeration of everything that can be Perceived by the player
 */
enum class Perception {
    BLOCKADE_BUMP,
    BREEZE,
    EXIT,
    FOOD,
    GLITTER,
    MOO,
    SCREAM,
    STENCH,
    WALL_BUMP
}

/**
 * Convert to GameObjects
 *
 * @param perceptions set of perceptions to convert
 *
 * @return [Set] of [GameObject]s that could be matched to given Perceptions
 */
fun toGameObjects(perceptions: Set<Perception>): Set<GameObject> {
    val gameObjects = mutableSetOf<GameObject>()
    perceptions.forEach { perception ->
        val gameObjectToMatch = perception.toGameObject() ?: return@forEach
        gameObjects.add(gameObjectToMatch)
    }
    return gameObjects.toSet()
}
