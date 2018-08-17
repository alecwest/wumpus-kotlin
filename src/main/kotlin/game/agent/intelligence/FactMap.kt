package game.agent.intelligence

import game.world.GameObject
import java.awt.Point

class FactMap(internal val factMap: MutableMap<Point, MutableSet<Pair<GameObject, Fact>>> = mutableMapOf()) {
    fun addFact(point: Point, fact: Fact, gameObject: GameObject) {
        if (factMap[point] == null) factMap[point] = mutableSetOf()
        factMap[point]!!.add(Pair(gameObject, fact))
    }
}

enum class Fact {
    HAS,
    HAS_NO
}