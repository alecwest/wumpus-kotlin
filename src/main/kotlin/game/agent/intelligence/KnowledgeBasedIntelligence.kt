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
    }

    private fun processPerceptions(world: World, commandResult: CommandResult) {
        val location = commandResult.getPlayerState().getLocation()
        val localObjects = getObjectsFromPerceptions(location, commandResult.getPerceptions())
        knowns[location] = mutableSetOf()
        localObjects.forEach { gameObjectToMatch ->
            val possibleNearbyObjects = gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldAffecting ->
                (worldAffecting.getFeature(WorldAffecting()) as WorldAffecting).createsObject(gameObjectToMatch)
            }
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