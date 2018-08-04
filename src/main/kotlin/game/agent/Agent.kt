package game.agent

import game.client.Client
import game.world.World

/**
 * Agent will be what actually plays the game using the intelligence provided to it
 */
class Agent(internal val client: Client) {
    internal val world: World = World(client.getWorldSize())
}