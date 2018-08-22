package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.*
import game.world.*
import game.world.GameObjectFeature.*
import game.world.effect.HereEffect
import util.*
import java.awt.Point

class KnowledgeBasedIntelligence2 : Intelligence() {
    internal val facts = FactMap()
    private lateinit var world: World
    private lateinit var commandResult: CommandResult

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        super.chooseNextMove(world, commandResult)
        processLastMove(world, commandResult)
        println(world.getWorldMap(commandResult.getPlayerState()))
        println(commandResult.toString())
        val inventoryItems = gameObjectsWithFeatures(setOf(Grabbable()))
                .filter { objectOrHereEffectInRoom(it) }.map { it.toInventoryItem() }

        return if (inventoryItems.isNotEmpty()) {
            GrabCommand(inventoryItems.first()!!)
        } else {
            bestExplorativeMove()
        }
    }

    private fun bestExplorativeMove(): Command {
        val playerState = commandResult.getPlayerState()
        val orderOfCommandPreferences = Direction.values().filter {
            it != playerState.getDirection() && canMoveInDirection(it)
        }.map {
            playerState.getLocation().adjacent(it)
        }.toMutableList()

        var noRoomFullyKnown = true
        // Ordered so any room not fully known shows first
        orderOfCommandPreferences.sortBy {
            val result = facts.featureFullyKnownInRoom(it, Perceptable())
            if (!result) noRoomFullyKnown = false
            result
        }

        if (noRoomFullyKnown && canMoveInDirection(playerState.getDirection())) {
            orderOfCommandPreferences.add(
                    0, playerState.getLocation().adjacent(playerState.getDirection()))
        }

        return orderOfCommandPreferences.firstOrNull().let {
            val directionOfRoom = it?.directionFrom(playerState.getLocation())
                    ?: playerState.getDirection()
            if (directionOfRoom == playerState.getDirection()) {
                MoveCommand()
            } else {
                TurnCommand(directionOfRoom)
            }
        }
    }

    internal fun objectOrHereEffectInRoom(gameObject: GameObject): Boolean {
        val worldEffects = gameObject.getFeature(WorldAffecting()) as WorldAffecting?
        return facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, gameObject) == TRUE
                || worldEffects?.effects?.any { effect ->
            effect::class == HereEffect::class
                    && facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, effect.gameObject) == TRUE
        } ?: false
    }

    private fun canMoveInDirection(direction: Direction): Boolean {
        val room = commandResult.getPlayerState().getLocation().adjacent(direction)
        for (gameObject in gameObjectsWithFeatures(setOf(Blocking()))) {
            if (facts.isTrue(room, HAS, gameObject) == TRUE) {
                return false
            }
        }
        return world.roomIsValid(room)
                && (facts.roomIsSafe(room) == TRUE || !dangerousEffectsInRoom(commandResult))
    }

    internal fun dangerousEffectsInRoom(commandResult: CommandResult): Boolean {
        return getEffectsInRoom(commandResult.getPlayerState().getLocation()).any { effect ->
                    effect.objectsThatCreateThis().any {
                        // Effect is not "dangerous" if the object creating it is in the
                        // same room, otherwise we'd be dead already
                        !(it.getFeature(WorldAffecting()) as WorldAffecting)
                                .effects.contains(HereEffect(effect))
                                && it.hasFeature(Dangerous())
                    }
        }
    }

    // Assumes you've already determined that the room in front is not safe
    internal fun turnToSafeRoom(world: World, commandResult: CommandResult): TurnCommand {
        val playerLocation = commandResult.getPlayerState().getLocation()
        val playerDirection = commandResult.getPlayerState().getDirection()
        val validRooms = playerLocation.adjacents().filter {
            it != playerLocation.adjacent(playerDirection) && world.roomIsValid(it)
        }
        val safeRooms = validRooms.filter { facts.roomIsSafe(it) == TRUE }
        val unknownRooms = validRooms.filter { !facts.everythingKnownAboutRoom(it) }

        val roomToTurnTo = safeRooms.firstOrNull {
            unknownRooms.contains(it)
        } ?: safeRooms.sortedBy {
            playerLocation.adjacent(playerDirection.right().right()) != it
        }.firstOrNull() ?: playerLocation.adjacent(playerDirection.right().right())

        return TurnCommand(roomToTurnTo.directionFrom(playerLocation) ?: playerDirection)
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
        val perceivedObjects = toGameObjects(commandResult.getPerceptions())
        val playerLocation = commandResult.getPlayerState().getLocation()
        gameObjectValues().forEach { gameObject ->
            if (gameObject.hasFeature(Blocking()) && perceivedObjects.contains(gameObject)) {
                addPerceivedBlocker(gameObject)
            } else {
                facts.addFact(
                        playerLocation,
                        if (perceivedObjects.contains(gameObject)) HAS else HAS_NO,
                        gameObject)
            }
        }
    }

    private fun addPerceivedBlocker(gameObject: GameObject) {
        val playerLocation = commandResult.getPlayerState().getLocation()
        val blockerLocation = playerLocation.adjacent(commandResult.getPlayerState().getDirection())
        facts.addFact(playerLocation, HAS_NO, gameObject)
        facts.addFact(blockerLocation, HAS, gameObject)
        world.addGameObject(blockerLocation, gameObject)
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