package game.agent

import game.client.Client

class Agent {
    internal lateinit var client: Client

    fun createGame(fileName: String = "", worldSize: Int = 10) {
        client = Client(fileName, worldSize)
    }
}