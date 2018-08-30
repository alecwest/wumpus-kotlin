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
    fun pathToRoom(point: Point? = null): Set<Point> {
        val pointPathsPair = dijkstra(commandResult.getPlayerState(), point)
        return generatePath(pointPathsPair.first, pointPathsPair.second)
    }

    private fun generatePath(target: Point, previous: ArrayList<Pair<Point, Direction>?>): Set<Point> {
        val path = arrayListOf<Point>()
        path.add(0, target)
        var targetIndex = world.getRoomIndex(target)
        while (previous[targetIndex] != null) {
            val targetRoom = previous[targetIndex]!!.first
            path.add(0, targetRoom)
            targetIndex = world.getRoomIndex(targetRoom)
        }
        path.removeAt(0) // TODO Do I really need to remove the starting point?
        return path.toSet()
    }

    private fun dijkstra(playerState: PlayerState, target: Point?): Pair<Point, ArrayList<Pair<Point, Direction>?>> {
        val startingLocation = playerState.getLocation()
        val startingDirection = playerState.getDirection()
        val vertices = mutableSetOf<Point>()
        val distances = arrayListOf<Pair<Int, Direction?>>()
        val previous = arrayListOf<Pair<Point, Direction>?>()
        var endingLocation = startingLocation

        for (i in 0 until world.getNumberRooms()) {
            distances.add(Pair(Int.MAX_VALUE, null))
            vertices.add(world.getRoomPoint(i))
            previous.add(null)
        }

        distances[world.getRoomIndex(startingLocation)] = Pair(0, startingDirection)

        while (vertices.isNotEmpty()) {
            val leastDistantRoom = getClosestRoom(vertices, distances)

            vertices.remove(leastDistantRoom)
            /**
             * If target is given, terminate when path to target is reached
             * Otherwise continue until first unknown, accessible room is found
             */
            if (leastDistantRoom == target ||
                    (target == null && !facts.featureFullyKnownInRoom(leastDistantRoom, Perceptable()))) {
                endingLocation = leastDistantRoom
                break
            }

            for (neighbor in leastDistantRoom.adjacents().filter {
                world.roomIsValid(it) && facts.roomIsSafe(it) == TRUE && facts.canEnterRoom(it) != FALSE }) {
                val currentIndex = world.getRoomIndex(leastDistantRoom)
                val neighborIndex = world.getRoomIndex(neighbor)
                val currentDirection = distances[currentIndex].second
                val alt = distances[currentIndex].first + costOfMoveToRoom(neighbor, leastDistantRoom, currentDirection)
                if (alt < distances[neighborIndex].first) {
                    distances[neighborIndex] = Pair(alt, neighbor.directionFrom(leastDistantRoom))
                    previous[neighborIndex] = Pair(leastDistantRoom, neighbor.directionFrom(leastDistantRoom)!!)
                }
            }
        }
        return Pair(endingLocation, previous)
    }

    private fun getClosestRoom(vertices: MutableSet<Point>, distances: ArrayList<Pair<Int, Direction?>>): Point {
        return vertices.minBy { point ->
            distances[world.getRoomIndex(point)].first
        } ?: Point(-1, -1)
    }

    internal fun costOfMoveToRoom(targetRoom: Point, currRoom: Point, currDirection: Direction?): Int {
        currDirection?.let {
            val targetDirection = targetRoom.directionFrom(currRoom) ?: return -1
            val playerState = PlayerState(location = currRoom, facing = it)
            return TurnCommand(targetDirection).getMoveCost(playerState) +
                    MoveCommand().getMoveCost(playerState)
        }
        return -1
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
