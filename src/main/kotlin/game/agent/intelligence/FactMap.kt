package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.world.GameObject
import java.awt.Point

class FactMap(internal val factMap: MutableMap<Point, MutableSet<Pair<GameObject, Fact>>> = mutableMapOf()) {
    fun addFact(point: Point, fact: Fact, gameObject: GameObject) {
        if (factMap[point] == null) factMap[point] = mutableSetOf()
        factMap[point]!!.add(Pair(gameObject, fact))
    }

    fun isTrue(point: Point, fact: Fact, gameObject: GameObject): Answer {
        if (factMap[point] == null) return UNKNOWN
        if (!factMap[point]!!.any { it.first == gameObject }) return UNKNOWN
        return if (factMap[point]!!.any { pair ->
                    pair.first == gameObject && pair.second == fact
                }) TRUE else FALSE
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