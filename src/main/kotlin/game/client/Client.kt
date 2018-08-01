package game.client

import game.command.Command
import game.command.CommandResult
import game.server.Server

/**
 * Client acts as the interface for the agent (human or AI) to communicate with the Server
 */
class Client(fileName: String = "", worldSize: Int = 10) {
    internal var sessionId = requestNewSessionId(fileName, worldSize)

    private fun requestNewSessionId(fileName: String, worldSize: Int): Int {
        return Server.newSession(fileName, worldSize)
    }

    fun makeMove(command: Command) {
        Server.makeMove(sessionId, command)
    }

    fun getMoveResult(): CommandResult? {
        return Server.getCommandResult(sessionId)
    }
}