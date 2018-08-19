package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import java.awt.Point

class KnowledgeBasedIntelligence2 : Intelligence() {
    internal val facts = FactMap()
    private lateinit var world: World
    private lateinit var commandResult: CommandResult

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        this.world = world
        this.commandResult = commandResult
        if(playerOnEdge()) {
            markEdgeRooms()
        }
        assessCurrentRoom()
        assessNearbyRooms()
        reassessForNewInsight()
    }

    internal fun playerOnEdge(): Boolean {
        return commandResult.getPlayerState().getLocation().adjacents().any {
            !world.roomIsValid(it)
        }
    }


    internal fun markEdgeRooms() {
        val edgeRooms = commandResult.getPlayerState().getLocation().adjacents().filter { !world.roomIsValid(it) }
        edgeRooms.forEach { edgeRoom ->
            gameObjectValues().forEach { gameObject ->
                facts.addFact(edgeRoom, HAS_NO, gameObject)
            }
        }
    }

    private fun assessCurrentRoom() {
        val perceivedObjects = toGameObjects(commandResult.getPerceptions().toSet()) // TODO change perceptions to a set
        val playerLocation = commandResult.getPlayerState().getLocation()
        gameObjectValues().forEach { gameObject ->
            facts.addFact(
                    playerLocation,
                    if (perceivedObjects.contains(gameObject)) HAS else HAS_NO,
                    gameObject)
        }
    }

    private fun assessNearbyRooms() {
        val playerLocation = commandResult.getPlayerState().getLocation()
        gameObjectsWithFeatures(setOf(WorldAffecting())).forEach { gameObject ->
            val worldAffecting = gameObject.getFeature(WorldAffecting()) as WorldAffecting
            worldAffecting.effects.forEach { worldEffect ->
                if (facts.isTrue(playerLocation, HAS_NO, worldEffect.gameObject) == TRUE) {
                    worldEffect.roomsAffected(playerLocation).forEach { roomAffected ->
                        facts.addFact(roomAffected, HAS_NO, gameObject)
                    }
                }
            }
        }
    }

    private fun reassessForNewInsight() {
        val factsToAdd = FactMap()
        facts.getMap().forEach { point, factSet ->
            val effectSet = factSet.filter { fact ->
                fact.second == HAS && fact.first.objectsThatCreateThis().isNotEmpty()
            }.map {
                fact -> fact.first
            }
            effectSet.forEach { effectGameObject ->
                val objectsThatCreateThis = effectGameObject.objectsThatCreateThis()
                objectsThatCreateThis.forEach { objectThatCreatesThis ->
                    val worldAffecting = objectThatCreatesThis.getFeature(WorldAffecting()) as WorldAffecting
                    val potentialRoomsLeftForObjectThatCreatesThis = mutableListOf<Point>()
                    worldAffecting.effects.forEach { worldEffect ->
                        if (worldEffect.gameObject == effectGameObject) {
                            potentialRoomsLeftForObjectThatCreatesThis.addAll(worldEffect.roomsAffected(point).filter { roomAffected ->
                                facts.isTrue(roomAffected, HAS_NO, objectThatCreatesThis) != TRUE
                            })
                        }
                    }
                    if (potentialRoomsLeftForObjectThatCreatesThis.size == 1) {
                        factsToAdd.addFact(potentialRoomsLeftForObjectThatCreatesThis[0], HAS, objectThatCreatesThis)
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