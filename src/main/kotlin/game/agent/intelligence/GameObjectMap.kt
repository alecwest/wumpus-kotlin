package game.agent.intelligence

import game.world.GameObject
import java.awt.Point

data class GameObjectMap(internal val gameObjectMap: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()) {
    fun add(point: Point, gameObject: GameObject) {
        val gameObjects = gameObjectMap.getOrDefault(point, mutableSetOf())
        gameObjects.add(gameObject)
        gameObjectMap[point] = gameObjects
    }

    fun remove(point: Point, gameObject: GameObject) {
        val gameObjects = gameObjectMap.getOrDefault(point, mutableSetOf())
        gameObjects.remove(gameObject)
        if (gameObjects.isEmpty()) {
            gameObjectMap.remove(point)
        } else {
            gameObjectMap[point] = gameObjects
        }
    }

    fun getMap(): Map<Point, Set<GameObject>> {
        return gameObjectMap.map {
            it.key to it.value.toSet()
        }.toMap()
    }
}