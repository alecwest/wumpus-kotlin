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
        val lastMove = client.getMoveResult()
        processLastMove(lastMove)
        val command = chooseNextMove()
        client.makeMove(command)
    }

    fun processLastMove(lastMove: CommandResult) {
    }

    internal fun chooseNextMove(world: World = this.world): Command {
        return intelligence.chooseNextMove(world)
    }
}