package game.client

import game.command.Command
import game.command.CommandResult
import game.server.Server

/**
 * @param fileName file containing world info, or blank indicating a random world generation
 * @param worldSize size of the world
 *
 * Client acts as the interface for the agent (human or AI) to communicate with the Server
 */
class Client(fileName: String = "", private val worldSize: Int = 10) {
    internal var sessionId = requestNewSessionId(fileName, worldSize)

    /**
     * @return [Int] id of a new session
     *
     * Create a new session with the Server
     */
    private fun requestNewSessionId(fileName: String, worldSize: Int): Int {
        return Server.newSession(fileName, worldSize)
    }

    fun getWorldSize() = worldSize

    /**
     * @param command Command to be executed
     *
     * Send command to the Server
     */
    fun makeMove(command: Command) {
        Server.makeMove(sessionId, command)
    }

    /**
     * @return [CommandResult] of the last move made by this Client
     */
    fun getMoveResult(): CommandResult {
        return Server.getCommandResult(sessionId)
    }
}