package game.agent.intelligence

import facts.Answer
import facts.Fact
import facts.FactMap
import game.command.CommandResult
import game.world.*
import util.adjacent
import util.adjacents
import java.awt.Point

/**
 * A static class to create as many facts as possible based off of a given [FactMap], [World], and [CommandResult]
 */
object FactProcessor {
    /**
     * @param facts existing facts
     * @param world existing world
     * @param commandResult result of last move
     *
     * Make changes to the given facts and world given the results of the last command
     */
    fun processLastMove(facts: FactMap, world: World, commandResult: CommandResult) {
        if(playerOnEdge(world, commandResult)) {
            markEdgeRooms(facts, world, commandResult)
        }
        assessCurrentRoom(facts, world, commandResult)
        assessNearbyRooms(facts, commandResult.getLocation())
        reassessForNewInsight(facts)
    }

    /**
     * Determine if the player is is on the edge of the world
     */
    internal fun playerOnEdge(world: World, commandResult: CommandResult): Boolean {
        return commandResult.getLocation().adjacents().any {
            !world.roomIsValid(it)
        }
    }

    /**
     * Mark nearby edge rooms as containing nothing
     */
    internal fun markEdgeRooms(facts: FactMap, world: World, commandResult: CommandResult) {
        val edgeRooms = commandResult.getLocation().adjacents().filter { !world.roomIsValid(it) }
        edgeRooms.forEach { edgeRoom ->
            gameObjectValues().forEach { gameObject ->
                facts.addFact(edgeRoom, Fact.HAS_NO, gameObject)
            }
        }
    }

    /**
     * Create facts for current room based on commandResult
     */
    internal fun assessCurrentRoom(facts: FactMap, world: World, commandResult: CommandResult) {
        val perceivedObjects = toGameObjects(commandResult.getPerceptions())
        val playerLocation = commandResult.getLocation()
        gameObjectValues().forEach { gameObject ->
            if (gameObject.hasFeature(GameObjectFeature.Blocking()) && perceivedObjects.contains(gameObject)) {
                addBlockingObject(facts, world, commandResult, gameObject)
            } else {
                facts.addFact(
                        playerLocation,
                        if (perceivedObjects.contains(gameObject)) Fact.HAS else Fact.HAS_NO,
                        gameObject)
            }
        }
    }

    /**
     * Add facts about blocking objects if they're encountered
     * This typically means adding a fact about the room in front of the player
     */
    internal fun addBlockingObject(facts: FactMap, world: World, commandResult: CommandResult, gameObject: GameObject) {
        if (!gameObject.hasFeature(GameObjectFeature.Blocking())) return
        val playerLocation = commandResult.getLocation()
        val blockerLocation = playerLocation.adjacent(commandResult.getDirection())
        facts.addFact(playerLocation, Fact.HAS_NO, gameObject)
        facts.addFact(blockerLocation, Fact.HAS, gameObject)
        world.addGameObjectAndEffects(blockerLocation, gameObject)
    }

    /**
     * Use knowledge in the [FactMap] to make inferences about other nearby rooms
     */
    internal fun assessNearbyRooms(facts: FactMap, playerLocation: Point) {
        gameObjectsWithFeatures(setOf(GameObjectFeature.WorldAffecting())).forEach { gameObject ->
            val worldAffecting = gameObject.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting
            worldAffecting.effects.forEach { worldEffect ->
                if (facts.isTrue(playerLocation, Fact.HAS_NO, worldEffect.gameObject) == Answer.TRUE) {
                    worldEffect.roomsAffected(playerLocation).forEach { roomAffected ->
                        facts.addFact(roomAffected, Fact.HAS_NO, gameObject)
                    }
                }
            }
        }
    }

    /**
     * After assessing everything, reassess to see if any new inferences can be made about any fact in any room
     */
    internal fun reassessForNewInsight(facts: FactMap) {
        val factsToAdd = FactMap()
        facts.getMap().forEach { fact ->
            val effectSet = facts.getEffectsInRoom(fact.key)
            effectSet.forEach { effectGameObject ->
                val objectsThatCreateThis = effectGameObject.objectsThatCreateThis()
                objectsThatCreateThis.forEach { objectThatCreatesThis ->
                    val worldAffecting = objectThatCreatesThis.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting
                    val potentialRoomsLeftForObjectThatCreatesThis = mutableListOf<Point>()
                    worldAffecting.effects.forEach { worldEffect ->
                        if (worldEffect.gameObject == effectGameObject) {
                            potentialRoomsLeftForObjectThatCreatesThis.addAll(worldEffect.roomsAffected(fact.key).filter { roomAffected ->
                                facts.isTrue(roomAffected, Fact.HAS_NO, objectThatCreatesThis) != Answer.TRUE
                            })
                        }
                    }
                    if (potentialRoomsLeftForObjectThatCreatesThis.size == 1) {
                        factsToAdd.addFact(potentialRoomsLeftForObjectThatCreatesThis[0], Fact.HAS, objectThatCreatesThis)
                    }
                }
            }
        }
        factsToAdd.getMap().forEach { point, factSet ->
            factSet.forEach { fact ->
                facts.addFact(point, fact.second, fact.first)
            }
        }
    }
}
