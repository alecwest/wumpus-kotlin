package server

import util.JsonParser.Companion.buildFromJsonFile
import game.world.World
import java.util.*

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
    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        buildFromJsonFile(fileName)
    }

    fun getWorldSize(): Int {
        return world.getSize()
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}

