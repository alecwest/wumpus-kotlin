package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.world.World
import game.world.gameObjectValues

abstract class Intelligence {
    open fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        processLastMove(world, commandResult)
        return MoveCommand()
    }

    open fun processLastMove(world: World, commandResult: CommandResult) {
        resetRoom(world, commandResult)
        for (gameObject in commandResult.getGameObjects()) {
            world.addGameObject(commandResult.getPlayerState().getLocation(), gameObject)
        }
    }

    private fun resetRoom(world: World, commandResult: CommandResult) {
        for (gameObject in gameObjectValues()) {
            world.removeGameObject(commandResult.getPlayerState().getLocation(), gameObject)
        }
    }
}