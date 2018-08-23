package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.world.GameObject
import game.world.GameObjectFeature
import game.world.GameObjectFeature.*
import game.world.gameObjectValues
import game.world.gameObjectsWithFeatures
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

    fun roomIsSafe(point: Point): Answer {
        if (gameObjectsWithFeatures(setOf(Dangerous())).any {
            when (isTrue(point, HAS, it)) {
                TRUE -> true
                FALSE -> false
                UNKNOWN -> return FALSE
            }
        }) return FALSE else return TRUE
    }

    // TODO knowing everything about a room in KnowledgeBasedIntelligence is currently impossible since only facts about perceptables are created upon assessment
    fun everythingKnownAboutRoom(point: Point): Boolean {
        for (gameObject in gameObjectValues()) {
            if (!factExists(point, gameObject)) return false
        }
        return true
    }

    fun featureFullyKnownInRoom(point: Point, feature: GameObjectFeature): Boolean {
        for (gameObject in gameObjectsWithFeatures(setOf(feature))) {
            if (!factExists(point, gameObject)) return false
        }
        return true
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