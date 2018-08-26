package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.world.World
import game.world.gameObjectValues
import game.world.toGameObject

abstract class Intelligence {
    open fun chooseNextMove(world: World, commandResult: CommandResult): List<Command> {
        processLastMove(world, commandResult)
        return listOf(MoveCommand())
    }

    internal open fun processLastMove(world: World, commandResult: CommandResult) {
        resetRoom(world, commandResult)
        for (perception in commandResult.getPerceptions()) {
            val gameObject = perception.toGameObject() ?: continue
            world.addGameObject(commandResult.getPlayerState().getLocation(), gameObject)
        }
    }

    internal fun resetRoom(world: World, commandResult: CommandResult) {
        for (gameObject in world.getGameObjects(commandResult.getPlayerState().getLocation())) {
            world.removeGameObject(commandResult.getPlayerState().getLocation(), gameObject)
        }
    }
}