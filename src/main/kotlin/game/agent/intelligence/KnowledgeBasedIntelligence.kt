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

class KnowledgeBasedIntelligence : Intelligence() {
    internal val facts = FactMap()
    internal lateinit var world: World
    internal lateinit var commandResult: CommandResult

    override fun chooseNextMoves(world: World, commandResult: CommandResult): List<Command> {
        super.chooseNextMoves(world, commandResult)
        processLastMove(world, commandResult)
        println(world.getWorldMap(commandResult.getPlayerState()))
        println(commandResult.toString())
        val inventoryItems = gameObjectsWithFeatures(setOf(Grabbable()))
                .filter { objectOrHereEffectInRoom(it) }.map { it.toInventoryItem() }

        return if (inventoryItems.isNotEmpty()) {
            listOf(GrabCommand(inventoryItems.first()!!))
        } else {
            bestExplorativeMoves(commandResult.getPlayerState())
        }
    }

    internal fun bestExplorativeMoves(playerState: PlayerState): List<Command> {
        val orderOfRoomPreferences = buildRoomPreferences(playerState)
        return toCommands(playerState, orderOfRoomPreferences.firstOrNull())
    }

    internal fun buildRoomPreferences(playerState: PlayerState): Set<Point> {
        val orderOfRoomPreferences = arrayListOf<Point>()
        val knownAndUncertainRooms = splitKnownAndUncertainRooms(getSafeRooms(playerState))

        knownAndUncertainRooms.first.sortBy { getTurnCount(playerState, it) }
        knownAndUncertainRooms.second.sortBy { getTurnCount(playerState, it) }

         orderOfRoomPreferences.addAll(knownAndUncertainRooms.second + knownAndUncertainRooms.first)

        return orderOfRoomPreferences.toSet()
    }

    internal fun getSafeRooms(playerState: PlayerState): Set<Point> {
        return Direction.values().filter { canMoveInDirection(it) }
                .map{ playerState.getLocation().adjacent(it) }.toSet()
    }

    internal fun splitKnownAndUncertainRooms(rooms: Set<Point>): Pair<ArrayList<Point>, ArrayList<Point>> {
        val knownAndUncertainRooms = Pair(arrayListOf<Point>(), arrayListOf<Point>())
        rooms.forEach {
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

    internal fun toCommands(playerState: PlayerState, point: Point?): List<Command> {
        val directionOfRoom = point?.directionFrom(playerState.getLocation())
                ?: playerState.getDirection()
        return listOf(if (directionOfRoom == playerState.getDirection()) {
            MoveCommand()
        } else {
            TurnCommand(directionOfRoom)
        })
    }

    internal fun objectOrHereEffectInRoom(gameObject: GameObject): Boolean {
        val worldEffects = gameObject.getFeature(WorldAffecting()) as WorldAffecting?
        return facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, gameObject) == TRUE
                || worldEffects?.effects?.any { effect ->
            effect::class == HereEffect::class
                    && facts.isTrue(commandResult.getPlayerState().getLocation(), HAS, effect.gameObject) == TRUE
        } ?: false
    }

    internal fun canMoveInDirection(direction: Direction): Boolean {
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
        return facts.getEffectsInRoom(commandResult.getPlayerState().getLocation()).any { effect ->
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
        FactProcessor.processLastMove(facts, this.world, this.commandResult)
    }
}
