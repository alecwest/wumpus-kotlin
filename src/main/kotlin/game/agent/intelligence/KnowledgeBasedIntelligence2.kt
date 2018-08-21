package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.*
import game.world.*
import game.world.GameObjectFeature.*
import game.world.effect.HereEffect
import util.adjacent
import util.adjacents
import util.left
import util.right
import java.awt.Point

class KnowledgeBasedIntelligence2 : Intelligence() {
    internal val facts = FactMap()
    private lateinit var world: World
    private lateinit var commandResult: CommandResult

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        processLastMove(world, commandResult)
        val inventoryItems = gameObjectsWithFeatures(setOf(Grabbable()))
                .filter { objectOrHereEffectInRoom(it) }.map { it.toInventoryItem() }

        if (inventoryItems.isNotEmpty()) {
            return GrabCommand(inventoryItems.first()!!)
        } else if (forwardFacingRoomIsSafe(commandResult)) {
            return MoveCommand()
        }
        return turnToSafeRoom(world, commandResult)
    }

    internal fun objectOrHereEffectInRoom(gameObject: GameObject): Boolean {
        val worldEffects = gameObject.getFeature(WorldAffecting()) as WorldAffecting?
        return facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, gameObject) == TRUE
                || worldEffects?.effects?.any { effect ->
            effect::class == HereEffect::class
                    && facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, effect.gameObject) == TRUE
        } ?: false
    }

    private fun forwardFacingRoomIsSafe(commandResult: CommandResult): Boolean {
        val forwardFacingRoom = commandResult.getPlayerState().getLocation()
                .adjacent(commandResult.getPlayerState().getDirection())
        return facts.roomIsSafe(forwardFacingRoom) == TRUE || !currentRoomHasDangers(commandResult)
    }

    private fun currentRoomHasDangers(commandResult: CommandResult): Boolean {
        return getEffectsInRoom(commandResult.getPlayerState().getLocation()).any { effect ->
            effect.objectsThatCreateThis().any { it.hasFeature(Dangerous()) }
        }
    }

    private fun turnToSafeRoom(world: World, commandResult: CommandResult): TurnCommand {
        val playerLocation = commandResult.getPlayerState().getLocation()
        val playerDirection = commandResult.getPlayerState().getDirection()
        val validRooms = playerLocation.adjacents().filter { world.roomIsValid(it) }
        val safeRooms = validRooms.filter { facts.roomIsSafe(it) == TRUE }
        return when {
            safeRooms.contains(playerLocation.adjacent(playerDirection.right())) -> TurnCommand(playerDirection.right())
            safeRooms.contains(playerLocation.adjacent(playerDirection.left())) -> TurnCommand(playerDirection.left())
            else -> TurnCommand(playerDirection.right().right())
        }
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
        facts.getMap().forEach { fact ->
            val effectSet = getEffectsInRoom(fact.key)
            effectSet.forEach { effectGameObject ->
                val objectsThatCreateThis = effectGameObject.objectsThatCreateThis()
                objectsThatCreateThis.forEach { objectThatCreatesThis ->
                    val worldAffecting = objectThatCreatesThis.getFeature(WorldAffecting()) as WorldAffecting
                    val potentialRoomsLeftForObjectThatCreatesThis = mutableListOf<Point>()
                    worldAffecting.effects.forEach { worldEffect ->
                        if (worldEffect.gameObject == effectGameObject) {
                            potentialRoomsLeftForObjectThatCreatesThis.addAll(worldEffect.roomsAffected(fact.key).filter { roomAffected ->
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

    private fun getEffectsInRoom(point: Point): Set<GameObject> {
        return facts.getMap().getOrDefault(point, setOf()).filter { fact ->
            fact.second == HAS && fact.first.objectsThatCreateThis().isNotEmpty()
        }.map {
            fact -> fact.first
        }.toSet()
    }
}