package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.command.TurnCommand
import game.world.World
import util.right

/**
 * BasicIntelligence is a proof on concept intelligence that either moves forward or turns right.
 */
class BasicIntelligence : Intelligence() {
    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        super.chooseNextMove(world, commandResult)
        return if (commandResult.moveRejected()) {
            TurnCommand(commandResult.getPlayerState().getDirection().right())
        } else {
            MoveCommand()
        }
    }
}