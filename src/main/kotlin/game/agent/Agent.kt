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

    /**
     * Use the assigned [Intelligence] to make the next moves
     */
    fun makeNextMoves() {
        chooseNextMoves().forEach {
            client.makeMove(it)
        }
    }

    /**
     * @param world the world the agent is in
     * @param lastMove the result of the agent's last move
     * @return [List] of commands to execute
     *
     * Use the assigned [Intelligence] to choose the next moves
     */
    internal fun chooseNextMoves(world: World = this.world, lastMove: CommandResult = client.getMoveResult()): List<Command> {
        return intelligence.chooseNextMoves(world, lastMove)
    }
}