package game.server

import game.Game
import game.GameState
import game.player.Player
import game.world.RoomContent
import game.world.World
import game.command.Command
import game.command.CommandInvoker
import util.JsonParser.Companion.buildFromJsonFile

/**
 * GameState is an observable object that exists in the game.server
 *      When the client wants to take an action, they call to the game.server (using the command
 *      pattern (Command, ShootCommand, GrabCommand, etc)), which forwards the command
 *      to the game state.
 *      The gameState executes the command, which can update data such as:
 *          Location of the player
 *          State of the player (inventoryItems, health, etc)
 *          State of the game (In Progress, Over, Goal Achieved, etc.)
 *      The gameState returns a state for the game.server to process and update the client with:
 *          ...
 */

/**
 * The game.server acts as an interface to the game for the client, allowing them to
 * make moves and learn the results of that move.
 */
// (private val fileName: String = "", private val worldSize: Int = 10)
object Server {
    private val sessions: HashMap<Int, Game> = hashMapOf()

    fun newSession(fileName: String = "", worldSize: Int = 10): Int {
        val id = sessions.size
        sessions[id] = createGame(fileName, worldSize)
        return id
    }

    private fun createGame(fileName: String, worldSize: Int): Game {
        return if (fileName.isBlank()) {
            Game(GameState(world = World(size = worldSize)))
        } else {
            buildFromJsonFile(fileName)
        }
    }

    internal fun getGame(id: Int) = sessions.getValue(id)

    fun makeMove(command: Command) {
        CommandInvoker.command = command
        CommandInvoker.performAction()
    }

    fun getPlayerState(id: Int): Player {
        return sessions.getValue(id).getPlayer()
    }

    fun getRoomContent(id: Int): ArrayList<RoomContent> {
        val game = sessions.getValue(id)
        return game.getRoomContent(game.getPlayerLocation())
    }
}

