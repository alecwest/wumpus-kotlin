package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.*
import game.player.PlayerState
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
        val orderOfRoomPreferences = buildRoomPreferences(playerState)

        return toCommand(playerState, orderOfRoomPreferences.firstOrNull())
    }


    // TODO needs cleaning again
    private fun buildRoomPreferences(playerState: PlayerState): Set<Point> {
        val orderOfRoomPreferences = arrayListOf<Point>()
        val knownAndUncertainRooms = splitAdjacentRoomsByKnownAndUncertainRooms(playerState)

        knownAndUncertainRooms.first.sortBy { getTurnCount(playerState, it) }
        knownAndUncertainRooms.second.sortBy { getTurnCount(playerState, it) }

         orderOfRoomPreferences.addAll(knownAndUncertainRooms.second + knownAndUncertainRooms.first)

        // TODO this line is intended for making sure we don't turn around in the face of
        // danger if we know we don't have to. It can probably get mixed in with splitAdjacentRooms...
//        if (knownAndUncertainRooms.first.contains(playerState.getLocation().adjacent(playerState.getDirection())) && canMoveInDirection(playerState.getDirection())) {
//            orderOfRoomPreferences.add(
//                    0, playerState.getLocation().adjacent(playerState.getDirection()))
//        }
        return orderOfRoomPreferences.toSet()
    }

    private fun splitAdjacentRoomsByKnownAndUncertainRooms(playerState: PlayerState): Pair<ArrayList<Point>, ArrayList<Point>> {
        val knownAndUncertainRooms = Pair(arrayListOf<Point>(), arrayListOf<Point>())
        Direction.values().filter {
            canMoveInDirection(it)
        }.map {
            playerState.getLocation().adjacent(it)
        }.toMutableList().forEach {
            val result = facts.featureFullyKnownInRoom(it, Perceptable())
            if (result) knownAndUncertainRooms.first.add(it)
            else knownAndUncertainRooms.second.add(it)
        }
        return knownAndUncertainRooms
    }

    internal fun getTurnCount(playerState: PlayerState, point: Point): Int? {
        var count = 0
        var currentDirection = playerState.getDirection()
        val targetDirection = point.directionFrom(playerState.getLocation()) ?: return null
        while (currentDirection != targetDirection) {
            count++
            currentDirection = if (currentDirection.left() == targetDirection)
                currentDirection.left() else currentDirection.right()
        }
        return count
    }

    private fun toCommand(playerState: PlayerState, point: Point?): Command {
        val directionOfRoom = point?.directionFrom(playerState.getLocation())
                ?: playerState.getDirection()
        return if (directionOfRoom == playerState.getDirection()) {
            MoveCommand()
        } else {
            TurnCommand(directionOfRoom)
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