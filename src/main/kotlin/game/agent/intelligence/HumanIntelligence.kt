package game.agent.intelligence

import game.command.*
import game.player.InventoryItem
import game.player.PlayerState
import game.world.World
import util.left
import util.right

class HumanIntelligence: Intelligence() {
    override fun chooseNextMove(world: World, commandResult: CommandResult): List<Command> {
        super.chooseNextMove(world, commandResult)
        println(world.getWorldMap(commandResult.getPlayerState()))
        println(commandResult.toString())
        var move = readLine() ?: ""
        while (true) {
            try {
                return listOf(move.toCommand(commandResult.getPlayerState()))
            } catch (e: Exception) {
                println(e.message)
                move = readLine() ?: ""
            }
        }
    }

    private fun String.toCommand(playerState: PlayerState): Command {
        return when (this) {
            "m" -> MoveCommand()
            "r" -> TurnCommand(playerState.getDirection().right())
            "l" -> TurnCommand(playerState.getDirection().left())
            "s" -> ShootCommand()
            "a" -> GrabCommand(InventoryItem.ARROW)
            "f" -> GrabCommand(InventoryItem.FOOD)
            "g" -> GrabCommand(InventoryItem.GOLD)
            else -> throw Exception("\"%s\" is an invalid move request. Try again.".format(this))
        }
    }
}

