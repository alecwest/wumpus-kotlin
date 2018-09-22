package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.world.World
import game.world.toGameObject

/**
 * Intelligence defines the basic actions for any intelligent agent
 */
abstract class Intelligence {
    /**
     * @param world the current [World]
     * @param commandResult the result of the last move
     * @return [List] of [commands][Command] taking the agent to their next move
     */
    open fun chooseNextMoves(world: World, commandResult: CommandResult): List<Command> {
        processLastMove(world, commandResult)
        return listOf(MoveCommand())
    }

    /**
     * @param world the current [World]
     * @param commandResult the result of the last move
     *
     * Apply knowledge attained from the [CommandResult] to the [World]
     */
    internal open fun processLastMove(world: World, commandResult: CommandResult) {
        resetRoom(world, commandResult)
        for (perception in commandResult.getPerceptions()) {
            val gameObject = perception.toGameObject() ?: continue
            world.addGameObjectAndEffects(commandResult.getLocation(), gameObject)
        }
    }

    /**
     * @param world the current [World]
     * @param commandResult the result of the last move
     *
     * Clear all information about the current room from the world, creating a blank slate
     * to assess what is perceived now in the [CommandResult]
     */
    internal fun resetRoom(world: World, commandResult: CommandResult) {
        for (gameObject in world.getGameObjects(commandResult.getLocation())) {
            world.removeGameObject(commandResult.getLocation(), gameObject)
        }
    }
}