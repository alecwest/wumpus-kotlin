package game.server

import game.Game
import game.GameState
import game.world.World
import game.command.Command
import game.command.CommandInvoker
import game.command.CommandResult
import util.JsonParser.buildFromJsonFile

/**
 * The server acts as an interface to the game for the client, allowing them to
 * make moves and learn the results of that move.
 */
object Server {
    private val sessions: HashMap<Int, Game> = hashMapOf()

    /**
     * Declare a new game session
     *
     * @param fileName world file to build off of
     * @param worldSize size of the world to create (unused if fileName is supplied)
     *
     * @return [Int] for id of new session
     */
    fun newSession(fileName: String = "", worldSize: Int = 10): Int {
        val id = sessions.size
        sessions[id] = createGame(fileName, worldSize)
        return id
    }

    /**
     * Create a new game
     *
     * @param fileName world file to build off of
     * @param worldSize size of the world to create (unused if fileName is supplied)
     *
     * @return [Game] new game
     */
    internal fun createGame(fileName: String, worldSize: Int): Game {
        return if (fileName.isBlank()) {
            Game(GameState(world = World(size = worldSize)))
        } else {
            buildFromJsonFile(fileName)
        }
    }

    /**
     * Get game from specific session
     *
     * @param id session id
     *
     * @return [Game]
     */
    internal fun getGame(id: Int) = sessions.getValue(id)

    /**
     * Execute a move on a specific session
     *
     * @param id session id
     * @param command command to execute
     */
    fun makeMove(id: Int, command: Command) {
        command.setGame(getGame(id))
        CommandInvoker.command = command
        CommandInvoker.performAction()
    }

    /**
     * Get the [CommandResult] of the last move in a specific session
     *
     * @param id session id
     *
     * @return [CommandResult]
     */
    fun getCommandResult(id: Int): CommandResult {
        return getGame(id).getCommandResult()
    }
}

