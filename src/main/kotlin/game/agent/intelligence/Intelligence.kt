package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.world.World

abstract class Intelligence {
    open fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        processLastMove(world, commandResult)
        return MoveCommand()
    }

    open fun processLastMove(world: World, commandResult: CommandResult) {
        for (roomContent in commandResult.getRoomContent()) {
            world.addRoomContent(commandResult.getPlayerState().getLocation(), roomContent)
        }
    }
}