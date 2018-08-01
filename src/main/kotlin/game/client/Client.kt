package game.client

import game.server.Server

/**
 * Client acts as the interface for the agent (human or AI) to communicate with the Server
 */
class Client(fileName: String = "", worldSize: Int = 10) {
    internal var sessionId = requestNewSessionId(fileName, worldSize)

    private fun requestNewSessionId(fileName: String, worldSize: Int): Int {
        return Server.newSession(fileName, worldSize)
    }
}