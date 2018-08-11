package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*
import util.adjacents
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val visited: MutableSet<Point> = mutableSetOf()
    internal val knowns: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()
    internal val possibles: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        visited.add(commandResult.getPlayerState().getLocation())
        processPerceptions(world, commandResult)
    }

    private fun processPerceptions(world: World, commandResult: CommandResult) {
        val location = commandResult.getPlayerState().getLocation()
        for (perception in commandResult.getPerceptions()) {
            val gameObjectToMatch = perception.toGameObject() ?: continue
            val objectsToAdd = gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldAffecting ->
                (worldAffecting.getFeature(WorldAffecting()) as WorldAffecting).createsObject(gameObjectToMatch)
            }
            objectsToAdd.forEach {
                for (adjacent in location.adjacents()) {
                    val gameObjects = possibles.getOrDefault(adjacent, mutableSetOf())
                    if (world.roomIsValid(adjacent)) {
                        gameObjects.add(it)
                        possibles[adjacent] = gameObjects
                    }
                }
            }
        }
    }

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }
}