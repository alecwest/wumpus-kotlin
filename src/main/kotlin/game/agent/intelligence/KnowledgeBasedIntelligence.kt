package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.GameObject
import game.world.GameObjectFeature.*
import game.world.World
import game.world.gameObjectsWithFeatures
import game.world.toGameObject
import util.adjacents
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val visited: MutableSet<Point> = mutableSetOf()
    internal val knowns: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()
    internal val possibles: MutableMap<Point, MutableSet<GameObject>> = mutableMapOf()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        visited.add(commandResult.getPlayerState().getLocation())
        val location = commandResult.getPlayerState().getLocation()
        for (perception in commandResult.getPerceptions()) {
            // Convert perceptable to game object
            val gameObjectToMatch = perception.toGameObject()
            // Get all objects that have effects
            val objectToAdd = gameObjectsWithFeatures(setOf(WorldAffecting())).filter { worldAffecting ->
                // Get the list of effects
                val worldEffects = (worldAffecting.getFeature(WorldAffecting()) as WorldAffecting)
                // look through each effect for the game object that was just perceived
                worldEffects.effects.any { worldEffect ->
                    worldEffect.gameObject == gameObjectToMatch
                }
            }
            objectToAdd.forEach {
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