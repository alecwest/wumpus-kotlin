package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.World

abstract class Intelligence {
    abstract fun chooseNextMove(world: World, commandResult: CommandResult): Command
}