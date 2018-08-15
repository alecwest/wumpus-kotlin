package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*
import game.world.effect.WorldEffect
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val knowns = GameObjectMap()
    internal val possibles = GameObjectMap()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        processPerceptions(world, commandResult)
        clearContradictions()
        makeDeductions()
    }

    // TODO needs refactoring
    private fun makeDeductions() {
        val knownsToAdd = mutableMapOf<Point, MutableSet<GameObject>>()
        var possibleCauseLocations: List<Point>
        knowns.getMap().forEach { point, gameObjects ->
            // look at each known object
            gameObjects.forEach { gameObject ->
                // get world affecting objects that could have caused the object currently being looked at
                val possibleCauses = gameObject.objectsThatCreateThis()
                if (possibleCauses.size == 1) {
                    possibleCauseLocations = getPossibleCauseLocations(point, possibleCauses[0])
                    if (possibleCauseLocations.size == 1) {
                        possibles.remove(possibleCauseLocations[0], possibleCauses[0])
                        knownsToAdd[possibleCauseLocations[0]] = mutableSetOf(possibleCauses[0])
                    }
                } else {
                    //TODO what if there are multiple possible causes? They need to be whittled down
                }
            }
        }
        knowns.add(knownsToAdd)
    }

    private fun getPossibleCauseLocations(effectLocation: Point, cause: GameObject): List<Point> {
        val locations = mutableListOf<Point>()
        val causesEffects = (cause.getFeature(WorldAffecting()) as WorldAffecting).effects
        causesEffects.filter { worldEffect ->
            val result = numberOfNearbyRoomsAffected(worldEffect, effectLocation)
            if (result.size == 1) {
                locations.addAll(result)
            }
            result.size == 1
        }
        return locations
    }

    private fun numberOfNearbyRoomsAffected(worldEffect: WorldEffect, effectLocation: Point): List<Point> {
        val locations = mutableListOf<Point>()
        worldEffect.roomsAffected(effectLocation).filter { roomAffected ->
            val result = possibles.getMap().containsKey(roomAffected)
            if (result) {
                locations.add(roomAffected)
            }
            result
        }
        return locations.toList()
    }

    // TODO needs refactoring
    private fun clearContradictions() {
        val pointsToRemove = mutableSetOf<Point>()
        possibles.getMap().forEach { point, gameObjects ->
            gameObjects.forEach { gameObject ->
                (gameObject.getFeature(WorldAffecting()) as WorldAffecting).effects.forEach { effect ->
                    effect.roomsAffected(point).forEach { roomAffected ->
                        if (!knowns.isNull(roomAffected) && knowns.getValue(roomAffected).isEmpty()) {
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
        val perceivedObjects = toGameObjects(commandResult.getPerceptions().toSet())
        knowns.add(location)

        perceivedObjects.forEach { gameObjectToMatch ->

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

    internal fun getPossibleEffects(commandResult: CommandResult, gameObject: GameObject): List<WorldEffect> {
        return (gameObject.getFeature(WorldAffecting()) as WorldAffecting).effects
                .filter { wasPerceived(commandResult, it.gameObject) }
    }

    internal fun wasPerceived(commandResult: CommandResult, gameObject: GameObject): Boolean {
        return commandResult.getPerceptions().any { it.toGameObject() == gameObject }
    }

    private fun addKnownObject(world: World, location: Point, objectToAdd: GameObject) {
        if (world.roomIsValid(location)) {
            knowns.add(location, objectToAdd)
        }
    }

    private fun addPossibleObject(world: World, location: Point, objectToAdd: GameObject) {
        if (world.roomIsValid(location) && knowns.isNull(location)) {
            possibles.add(location, objectToAdd)
        }
    }

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }
}
