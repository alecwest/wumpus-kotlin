package server

import game.Game
import game.GameState
import game.player.Player
import game.world.World
import server.command.Command
import server.command.CommandInvoker
import util.JsonParser.Companion.buildFromJsonFile

/**
 * GameState is an observable object that exists in the server
 *      When the client wants to take an action, they call to the server (using the command
 *      pattern (Command, ShootCommand, GrabCommand, etc)), which forwards the command
 *      to the game state.
 *      The gameState executes the command, which can update data such as:
 *          Location of the player
 *          State of the player (inventoryItems, health, etc)
 *          State of the game (In Progress, Over, Goal Achieved, etc.)
 *      The gameState returns a state for the server to process and update the client with:
 *          ...
 */
class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    private val game: Game = if (fileName.isBlank()) {
        Game(GameState(world = World(size = worldSize)))
    } else {
        buildFromJsonFile(fileName)
    }

    fun getGame() = game

    fun getWorldSize(): Int {
        return game.getWorldSize()
    }

    fun getNumberRooms(): Int {
        return game.getNumberRooms()
    }

    fun makeMove(command: Command) {
        println("Making move " + command.toString())
        CommandInvoker.command = command
        CommandInvoker.performAction()
    }

    fun getPlayerState(): Player {
        return game.getPlayer()
    }
}

