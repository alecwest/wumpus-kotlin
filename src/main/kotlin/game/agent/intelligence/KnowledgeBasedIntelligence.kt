package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*
import game.world.effect.WorldEffect
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val knowns: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()
    internal val possibles: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        processPerceptions(world, commandResult)
        clearContradictions()
        makeDeductions()
    }

    // TODO needs refactoring
    private fun makeDeductions() {
        // take everything we know
        val knownsToAdd = mutableMapOf<Point, MutableSet<GameObject>>()
        var possibleToRemove: Point = Point(-1, -1)
        knowns.forEach { point, gameObjects ->
            // look at each known object
            gameObjects.forEach { gameObject ->
                // get world affecting objects that could have caused the object currently being looked at
                val possibleCauses = gameObject.objectsThatCreateThis()
                if (possibleCauses.size == 1) {
                    if ((possibleCauses[0].getFeature(WorldAffecting()) as WorldAffecting).effects.filter { worldEffect ->
                                worldEffect.roomsAffected(point).filter { roomAffected ->
                                    val result = possibles.containsKey(roomAffected)
                                    if (result) {
                                        possibleToRemove = roomAffected
                                    }
                                    result
                                }.size == 1
                            }.size == 1) {
                        val possiblesInRoom = possibles.getOrDefault(possibleToRemove, mutableSetOf())
                        possiblesInRoom.remove(possibleCauses[0])
                        if (possiblesInRoom.isEmpty()) {
                            possibles.remove(possibleToRemove)
                        } else {
                            possibles[possibleToRemove] = possiblesInRoom
                        }
                        knownsToAdd[possibleToRemove] = mutableSetOf(possibleCauses[0])
                    }
                } else {
                    //TODO what if there are multiple possible causes? They need to be whittled down
                }
            }
        }
        knownsToAdd.forEach {
            val gameObjects = knowns.getOrDefault(it.key, mutableSetOf())
            gameObjects.addAll(it.value)
            knowns[it.key] = gameObjects
        }
    }

    // TODO needs refactoring
    private fun clearContradictions() {
        val pointsToRemove = mutableSetOf<Point>()
        possibles.forEach { point, gameObjects ->
            gameObjects.forEach { gameObject ->
                (gameObject.getFeature(WorldAffecting()) as WorldAffecting).effects.forEach { effect ->
                    effect.roomsAffected(point).forEach { roomAffected ->
                        if (knowns[roomAffected]?.isEmpty() == true) {
                            pointsToRemove.add(point)
                            return@forEach
                        }
                    }
                }
            }
        }
        pointsToRemove.forEach {
            possibles.remove(it)
        }
    }

    private fun processPerceptions(world: World, commandResult: CommandResult) {
        val location = commandResult.getPlayerState().getLocation()
        val localObjects = getObjectsFromPerceptions(location, commandResult.getPerceptions())
        knowns[location] = mutableSetOf()
        localObjects.forEach { gameObjectToMatch ->
            val possibleNearbyObjects = gameObjectToMatch.objectsThatCreateThis()
            possibleNearbyObjects.forEach {possibleNearbyObject ->
                val possibleEffects = getPossibleEffects(commandResult, possibleNearbyObject)
                possibleEffects.forEach { worldEffect ->
                    worldEffect.roomsAffected(location).forEach { point ->
                        when (point) {
                            location -> addKnownObject(world, point, possibleNearbyObject)
                            else -> addPossibleObject(world, point, possibleNearbyObject)
                        }
                    }
                }
            }
            addKnownObject(world, location, gameObjectToMatch)
        }
    }

    private fun getPossibleEffects(commandResult: CommandResult, gameObject: GameObject): List<WorldEffect> {
        return (gameObject.getFeature(WorldAffecting()) as WorldAffecting).effects
                .filter { wasPerceived(commandResult, it.gameObject) }
    }

    private fun wasPerceived(commandResult: CommandResult, gameObject: GameObject): Boolean {
        return commandResult.getPerceptions().any { it.toGameObject() == gameObject }
    }

    private fun getObjectsFromPerceptions(location: Point, perceptions: ArrayList<Perception>): MutableSet<GameObject> {
        val knownObjects = knowns.getOrDefault(location, mutableSetOf())
        perceptions.forEach { perception ->
            val gameObjectToMatch = perception.toGameObject() ?: return@forEach
            knownObjects.add(gameObjectToMatch)
        }
        return knownObjects
    }

    private fun addKnownObject(world: World, location: Point, objectToAdd: GameObject) {
        if (world.roomIsValid(location)) {
            val gameObjects = knowns.getOrDefault(location, mutableSetOf())
            gameObjects.add(objectToAdd)
            knowns[location] = gameObjects
        }
    }

    private fun addPossibleObject(world: World, location: Point, objectToAdd: GameObject) {
        if (world.roomIsValid(location) && knowns[location] == null) {
            val gameObjects = possibles.getOrDefault(location, mutableSetOf())
            gameObjects.add(objectToAdd)
            possibles[location] = gameObjects
        }
    }

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }
}