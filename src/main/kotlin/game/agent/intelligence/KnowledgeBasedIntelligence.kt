package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val knowns: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()
    internal val possibles: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        processPerceptions(world, commandResult)
    }

    // TODO this can be refactored
    private fun processPerceptions(world: World, commandResult: CommandResult) {
        val location = commandResult.getPlayerState().getLocation()
        val localObjects = getObjectsFromPerceptions(location, commandResult.getPerceptions())

        localObjects.forEach { gameObjectToMatch ->
            val possibleNearbyObjects = gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldAffecting ->
                (worldAffecting.getFeature(WorldAffecting()) as WorldAffecting).createsObject(gameObjectToMatch)
            }
            possibleNearbyObjects.forEach {
                // TODO this isn't complete if a moo were perceived
                for (adjacent in location.adjacents()) {
                    addPossibleObject(world, adjacent, it)
                }
            }
        }
        knowns[location] = localObjects
    }

    private fun getObjectsFromPerceptions(location: Point, perceptions: ArrayList<Perception>): MutableSet<GameObject> {
        val knownObjects = knowns.getOrDefault(location, mutableSetOf())
        perceptions.forEach { perception ->
            val gameObjectToMatch = perception.toGameObject() ?: return@forEach
            knownObjects.add(gameObjectToMatch)
        }
        return knownObjects
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