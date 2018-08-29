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

    /**
     * pathToRoom should consider the fact that each turn+move combo costs 2 moves,
     * meaning the diagonal path is not necessarily the best
     */
    fun pathToRoom(point: Point): Set<Point> {
        val path = mutableSetOf<Point>()

        return path
    }

    private fun dijkstra(playerLocation: Point) {
        val vertices = mutableSetOf<Point>()
        val distances = arrayListOf<Int>()
        val previous = arrayListOf<Point>()

        for (i in 1..world.getNumberRooms()) {
            distances.add(Int.MAX_VALUE)
            vertices.add(world.getRoomPoint(i))
        }

        distances[world.getRoomIndex(playerLocation)] = 0

        while (vertices.isNotEmpty()) {
            val leastDistantRoom = getClosestRoom(distances)

            vertices.remove(leastDistantRoom)

            for (neighbor in leastDistantRoom.adjacents()) {
                val leastDistantIndex = world.getRoomIndex(leastDistantRoom)
                val alt = distances[leastDistantIndex] + costOfMoveToRoom()
                if (alt < distances[leastDistantIndex]) {
                    distances[leastDistantIndex] = alt
                    previous[leastDistantIndex] = leastDistantRoom
                }
            }
        }
    }

    private fun getClosestRoom(distances: ArrayList<Int>): Point {
        return world.getRoomPoint(distances.min() ?: -1)
    }

    private fun costOfMoveToRoom(): Int {
        return 1
    }

    internal fun buildRoomPreferences(playerState: PlayerState): Set<Point> {
        val orderOfRoomPreferences = arrayListOf<Point>()
        val knownAndUncertainRooms = splitKnownAndUncertainRooms(getSafeRooms(playerState))

        knownAndUncertainRooms.first.sortBy { toCommands(playerState, it).map { it.getMoveCost(playerState) }.sum() }
        knownAndUncertainRooms.second.sortBy { toCommands(playerState, it).map { it.getMoveCost(playerState) }.sum() }

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

    internal fun toCommands(playerState: PlayerState, point: Point?): List<Command> {
        val directionOfRoom = point?.directionFrom(playerState.getLocation())
                ?: playerState.getDirection()
        return if (directionOfRoom == playerState.getDirection()) {
            listOf(MoveCommand())
        } else {
            listOf(TurnCommand(directionOfRoom), MoveCommand())
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
