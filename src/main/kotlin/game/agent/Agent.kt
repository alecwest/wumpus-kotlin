package game.agent

import game.agent.intelligence.Intelligence
import game.client.Client
import game.world.World

/**
 * Agent will be what actually plays the game using the intelligence provided to it
 */
class Agent(internal val client: Client, internal val intelligence: Intelligence) {
    internal val world: World = World(client.getWorldSize())

    fun processLastMove() {
        val moveResult = client.getMoveResult()
    }

    fun makeNextMove() {
        val command = intelligence.chooseNextMove(world)
        client.makeMove(command)
    }
}