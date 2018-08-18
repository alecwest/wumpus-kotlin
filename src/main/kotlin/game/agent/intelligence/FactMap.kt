package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.world.GameObject
import java.awt.Point

class FactMap(private val factMap: MutableMap<Point, MutableSet<Pair<GameObject, Fact>>> = mutableMapOf()) {
    fun getMap(): Map<Point, Set<Pair<GameObject, Fact>>> {
        return factMap.map {
            it.key to it.value.toSet()
        }.toMap()
    }

    fun addFact(point: Point, fact: Fact, gameObject: GameObject) {
        if (factMap[point] == null) factMap[point] = mutableSetOf()
        factMap[point]!!.removeIf { it.first == gameObject }
        factMap[point]!!.add(Pair(gameObject, fact))
    }

    fun isTrue(point: Point, fact: Fact, gameObject: GameObject): Answer {
        return  if (!factExists(point, gameObject)) UNKNOWN
                else if (factMap[point]!!.any { pair ->
                    pair.first == gameObject && pair.second == fact
                }) TRUE else FALSE
    }

    fun factExists(point: Point, gameObject: GameObject): Boolean {
        return factMap[point] != null && factMap[point]!!.any { it.first == gameObject }
    }
}

enum class Fact {
    HAS,
    HAS_NO
}

enum class Answer {
    TRUE,
    FALSE,
    UNKNOWN
}