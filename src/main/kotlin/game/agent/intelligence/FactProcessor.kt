package game.agent.intelligence

import game.command.CommandResult
import game.world.*
import util.adjacent
import util.adjacents
import java.awt.Point

object FactProcessor {
    fun processLastMove(facts: FactMap, world: World, commandResult: CommandResult) {
        if(playerOnEdge(world, commandResult)) {
            markEdgeRooms(facts, world, commandResult)
        }
        assessCurrentRoom(facts, world, commandResult)
        assessNearbyRooms(facts, commandResult.getLocation())
        reassessForNewInsight(facts)
    }

    internal fun playerOnEdge(world: World, commandResult: CommandResult): Boolean {
        return commandResult.getLocation().adjacents().any {
            !world.roomIsValid(it)
        }
    }

    internal fun markEdgeRooms(facts: FactMap, world: World, commandResult: CommandResult) {
        val edgeRooms = commandResult.getLocation().adjacents().filter { !world.roomIsValid(it) }
        edgeRooms.forEach { edgeRoom ->
            gameObjectValues().forEach { gameObject ->
                facts.addFact(edgeRoom, Fact.HAS_NO, gameObject)
            }
        }
    }

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

    internal fun addBlockingObject(facts: FactMap, world: World, commandResult: CommandResult, gameObject: GameObject) {
        if (!gameObject.hasFeature(GameObjectFeature.Blocking())) return
        val playerLocation = commandResult.getLocation()
        val blockerLocation = playerLocation.adjacent(commandResult.getDirection())
        facts.addFact(playerLocation, Fact.HAS_NO, gameObject)
        facts.addFact(blockerLocation, Fact.HAS, gameObject)
        world.addGameObject(blockerLocation, gameObject)
    }

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
