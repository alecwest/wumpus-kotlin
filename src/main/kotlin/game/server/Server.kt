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
class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    private val game: Game = if (fileName.isBlank()) {
        Game(GameState(world = World(size = worldSize)))
    } else {
        buildFromJsonFile(fileName)
    }

    internal fun getGame() = game

    fun makeMove(command: Command) {
        CommandInvoker.command = command
        CommandInvoker.performAction()
    }

    fun getPlayerState(): Player {
        return game.getPlayer()
    }

    fun getRoomContent(): ArrayList<RoomContent> {
        return game.getRoomContent(game.getPlayerLocation())
    }
}

