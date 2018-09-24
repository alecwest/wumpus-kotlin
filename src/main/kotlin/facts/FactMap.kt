package facts

import facts.Answer.*
import facts.Fact.*
import game.world.GameObject
import game.world.GameObjectFeature
import game.world.GameObjectFeature.*
import game.world.gameObjectValues
import game.world.gameObjectsWithFeatures
import java.awt.Point

/**
 * A mapping of facts
 *
 * This class holds a collection of facts, allowing more
 * information to be gathered and inferred
 */
class FactMap(private val factMap: MutableMap<Point, MutableSet<Pair<GameObject, Fact>>> = mutableMapOf()) {
    /**
     * @return An immutable copy of the map
     */
    fun getMap(): Map<Point, Set<Pair<GameObject, Fact>>> {
        return factMap.map {
            it.key to it.value.toSet()
        }.toMap()
    }

    /**
     * Adds facts in a sentence-like manner to the fact map
     */
    fun addFact(point: Point, fact: Fact, gameObject: GameObject) {
        if (factMap[point] == null) factMap[point] = mutableSetOf()
        factMap[point]!!.removeIf { it.first == gameObject }
        factMap[point]!!.add(Pair(gameObject, fact))
    }

    /**
     * @return [Answer] indicating truthfulness of a statement
     */
    fun isTrue(point: Point, fact: Fact, gameObject: GameObject): Answer {
        return  if (!factExists(point, gameObject)) UNKNOWN
                else if (factMap[point]!!.any { pair ->
                    pair.first == gameObject && pair.second == fact
                }) TRUE else FALSE
    }

    /**
     * @return [Boolean] indicating whether or not a fact about the given object exists
     */
    fun factExists(point: Point, gameObject: GameObject): Boolean {
        return factMap[point] != null && factMap[point]!!.any { it.first == gameObject }
    }

    /**
     * @param point coordinates of room
     * @return [Answer] indicating safety of room
     */
    fun roomIsSafe(point: Point): Answer {
        if (gameObjectsWithFeatures(setOf(Dangerous())).any {
            when (isTrue(point, HAS, it)) {
                TRUE -> true
                FALSE -> false
                UNKNOWN -> return FALSE
            }}) return FALSE else return TRUE
    }

    /**
     * @param point coordinates of room
     * @return [Answer] indicating whether there is something known that is
     * preventing the player from entering the given room
     */
    fun canEnterRoom(point: Point): Answer {
        val blockingObjects = gameObjectsWithFeatures(setOf(Blocking()))
        var numFalse = 0
        blockingObjects.forEach {
            val hasBlockingObject = isTrue(point, HAS, it)
            if (hasBlockingObject == TRUE) return FALSE
            else if (hasBlockingObject == FALSE) numFalse++
        }
        return if (numFalse == blockingObjects.size) TRUE else UNKNOWN
    }

    /**
     * @param point coordinates of room
     * @return [Boolean] indicating whether or not a fact exists for all
     * GameObject values in the given room
     */
    fun everythingKnownAboutRoom(point: Point): Boolean {
        // TODO knowing everything about a room in KnowledgeBasedIntelligence is currently impossible since only facts about perceptables are created upon assessment
        for (gameObject in gameObjectValues()) {
            if (!factExists(point, gameObject)) return false
        }
        return true
    }

    /**
     * @param point coordinates of room
     * @param feature specific [GameObjectFeature]
     * @return [Boolean] indicating whether or not a fact exists for all
     * GameObjects with the given feature in the room
     */
    fun featureFullyKnownInRoom(point: Point, feature: GameObjectFeature): Boolean {
        for (gameObject in gameObjectsWithFeatures(setOf(feature))) {
            if (!factExists(point, gameObject)) return false
        }
        return true
    }

    /**
     * @param point coordinates of room
     * @return [Set] of all [GameObjects][GameObject] in a given room
     */
    fun getEffectsInRoom(point: Point): Set<GameObject> {
        return getMap().getOrDefault(point, setOf()).filter { fact ->
            fact.second == HAS && fact.first.objectsThatCreateThis().isNotEmpty()
        }.map {
            fact -> fact.first
        }.toSet()
    }

    /**
     * @param gameObject object to look for
     * @return [Set] of all points[Point] that are known to have a specific [GameObject]
     */
    fun roomsWithObject(gameObject: GameObject): Set<Point> {
        return getMap().filter { isTrue(it.key, HAS, gameObject) == TRUE }
                .map { it.key }.toSet()
    }
}
