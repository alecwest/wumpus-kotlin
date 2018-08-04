package game.agent.intelligence

import game.command.Command
import game.world.World

abstract class Intelligence {
    abstract fun chooseNextMove(world: World): Command
}