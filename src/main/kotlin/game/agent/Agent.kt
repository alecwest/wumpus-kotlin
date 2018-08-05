package game.agent

import game.agent.intelligence.Intelligence
import game.client.Client
import game.command.Command
import game.command.CommandResult
import game.world.World

/**
 * Agent will be what actually plays the game using the intelligence provided to it
 */
class Agent(internal val client: Client, internal val intelligence: Intelligence) {
    internal val world: World = World(client.getWorldSize())

    fun makeNextMove() {
        val command = chooseNextMove()
        client.makeMove(command)
    }

    internal fun chooseNextMove(world: World = this.world, lastMove: CommandResult = client.getMoveResult()): Command {
        return intelligence.chooseNextMove(world, lastMove)
    }
}