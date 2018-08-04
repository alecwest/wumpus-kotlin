package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.World

abstract class Intelligence {
    abstract fun chooseNextMove(world: World, commandResult: CommandResult): Command

    open fun processLastMove(world: World, commandResult: CommandResult) {
        for (roomContent in commandResult.getRoomContent()) {
            world.addRoomContent(commandResult.getPlayerState().getLocation(), roomContent)
        }
    }
}