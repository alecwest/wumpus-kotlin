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
        val knownObjects = knowns.getOrDefault(location, mutableSetOf())
        for (perception in commandResult.getPerceptions()) {
            val gameObjectToMatch = perception.toGameObject() ?: continue
            knownObjects.add(gameObjectToMatch)

            val objectsToAdd = gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldAffecting ->
                (worldAffecting.getFeature(WorldAffecting()) as WorldAffecting).createsObject(gameObjectToMatch)
            }
            objectsToAdd.forEach {
                for (adjacent in location.adjacents()) {
                    if (world.roomIsValid(adjacent) && knowns[adjacent] == null) {
                        val gameObjects = possibles.getOrDefault(adjacent, mutableSetOf())
                        gameObjects.add(it)
                        possibles[adjacent] = gameObjects
                    }
                }
            }
        }
        knowns[location] = knownObjects
    }

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }
}