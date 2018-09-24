package game.agent.intelligence

import facts.Answer.*
import facts.Fact.*
import facts.FactMap
import game.command.*
import game.player.InventoryItem
import game.player.PlayerState
import game.world.*
import game.world.GameObjectFeature.*
import game.world.effect.HereEffect
import util.*
import java.awt.Point

/**
 * An Intelligence implementation that uses a knowledge base to discover the next best moves
 */
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

        return when {
            commandResult.hasItem(InventoryItem.GOLD) -> exit(commandResult.getPlayerState())
            inventoryItems.isNotEmpty() -> listOf(GrabCommand(inventoryItems.first()!!))
            else -> bestExplorativeMoves(commandResult.getPlayerState())
        }
    }

    /**
     * @param playerState information on the player's location
     * @return [List] of [commands][Command] leading to the nearest room with an [EXIT][GameObject.EXIT]
     */
    internal fun exit(playerState: PlayerState): List<Command> {
        val commands = buildCommandSequence(playerState,
                facts.roomsWithObject(GameObject.EXIT)).toMutableList()
        commands.add(ExitCommand())
        return commands
    }

    /**
     * @param playerState information on the player's location
     * @return [List] of [commands][Command] leading to the closest safe and unexplored room
     */
    internal fun bestExplorativeMoves(playerState: PlayerState): List<Command> {
        return buildCommandSequence(playerState, emptySet())
    }

    /**
     * @param playerState information on the player's location
     * @param targetRooms a [Set] of rooms that are preferred
     * @return [List] of [commands][Command] leading to the nearest room in the set of targetRooms
     */
    private fun buildCommandSequence(playerState: PlayerState, targetRooms: Set<Point>): List<Command> {
        val commands = mutableListOf<Command>()
        var currPlayerState = playerState
        pathToRoom(targetRooms).forEach {
            commands.addAll(toCommands(currPlayerState, it))
            currPlayerState = currPlayerState.copyThis(
                    location = it,
                    facing = it.directionFrom(currPlayerState.getLocation())
                            ?: currPlayerState.getDirection())
        }
        return commands
    }

    /**
     * @param possibleTargets a [Set] of rooms that are preferred
     * @return [Set] of points building a path to the nearest room in the set of targets
     *
     * Uses path-finding to determine the fastest route to the nearest room in the set
     */
    fun pathToRoom(possibleTargets: Set<Point> = setOf()): Set<Point> {
        val pointPathsPair = dijkstra(commandResult.getPlayerState(), possibleTargets)
        return generatePath(pointPathsPair.first, pointPathsPair.second)
    }

    /**
     * @param target the target [Point]
     * @param previous a list of pairs and directions
     * @return [Set] of points
     *
     * Use results from shortest-path algorithm to generate the path to a target room
     */
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

    /**
     * @param playerState information on the player's location
     * @param targets a [Set] of preferred rooms to find a path to
     * @return [Pair] indicating the [Point] a path was found to and the list of points that can be used to lead the
     * player there
     *
     * An implementation of dijkstra's algorithm on the game world
     */
    private fun dijkstra(playerState: PlayerState, targets: Set<Point> = setOf()): Pair<Point, ArrayList<Pair<Point, Direction>?>> {
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
            if (targets.contains(leastDistantRoom) ||
                    (targets.isEmpty() && !facts.featureFullyKnownInRoom(leastDistantRoom, Perceptable()))) {
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

    /**
     * @param vertices list of points
     * @param distances list of distances associated with the points
     * @return [Point] with the shortest distance
     */
    private fun getClosestRoom(vertices: MutableSet<Point>, distances: ArrayList<Pair<Int, Direction?>>): Point {
        return vertices.minBy { point ->
            distances[world.getRoomIndex(point)].first
        }!!
    }

    /**
     * @param targetRoom the room to move to
     * @param currRoom the room the agent is currently in
     * @param currDirection the direction the player is currently facing
     * @return [Int] indicating the cost of moving to the room, -1 if targetRoom is not adjacent
     */
    internal fun costOfMoveToRoom(targetRoom: Point, currRoom: Point, currDirection: Direction?): Int {
        currDirection?.let {
            val targetDirection = targetRoom.directionFrom(currRoom) ?: return -1
            val playerState = PlayerState(location = currRoom, facing = it)
            return TurnCommand(targetDirection).getMoveCost(playerState) +
                    MoveCommand().getMoveCost(playerState)
        }
        return -1
    }

    /**
     * @param playerState information about the player
     * @param target adjacent room
     * @return [List] of commands leading to the target room
     */
    internal fun toCommands(playerState: PlayerState, point: Point?): List<Command> {
        val directionOfRoom = point?.directionFrom(playerState.getLocation())
                ?: playerState.getDirection()
        return if (directionOfRoom == playerState.getDirection()) {
            listOf(MoveCommand())
        } else {
            listOf(TurnCommand(directionOfRoom), MoveCommand())
        }
    }

    /**
     * @param gameObject object being checked for
     * @return [Boolean]
     *
     * Check for a specific [GameObject] or a [HereEffect] that it may have placed in the room
     */
    internal fun objectOrHereEffectInRoom(gameObject: GameObject): Boolean {
        val worldEffects = gameObject.getFeature(WorldAffecting()) as WorldAffecting?
        return facts.isTrue(commandResult.getLocation(), HAS, gameObject) == TRUE
                || worldEffects?.effects?.any { effect ->
            effect::class == HereEffect::class
                    && facts.isTrue(commandResult.getLocation(), HAS, effect.gameObject) == TRUE
        } ?: false
    }

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        this.world = world
        this.commandResult = commandResult
        FactProcessor.processLastMove(facts, this.world, this.commandResult)
    }
}
