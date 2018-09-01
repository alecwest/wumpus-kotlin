package game.server

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.GameObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import game.command.*
import game.world.Perception
import org.junit.jupiter.api.Assertions.assertTrue
import util.*
import java.awt.Point

class ServerTest {
    private val gameId = Server.newSession()
    private val initialPoint = Point(0, 0)
    private val initialDirection = Direction.NORTH
    private val initialInventory = mapOf(InventoryItem.ARROW to 1)

    @Test
    fun `server generates world from world size only`() {
        val worldSize = 10
        val gameId = Server.newSession(worldSize = worldSize)

        assertEquals(worldSize, Server.getGame(gameId).getWorldSize())
        assertEquals(worldSize * worldSize, Server.getGame(gameId).getNumberRooms())
    }

    @Test
    fun `server generates world from json file`() {
        val gameId = Server.newSession(fileName = Helpers.testFilePath + "testFile.json")

        assertEquals(11, Server.getGame(gameId).getWorldSize())
        assertEquals(121, Server.getGame(gameId).getNumberRooms())
    }

    // TODO add bad move tests
    @Test
    fun `check player moves on move command`() {
        assertEquals(initialPoint, Server.getCommandResult(gameId).getPlayerState().getLocation())
        Server.makeMove(gameId, MoveCommand())
        assertEquals(initialPoint.north(), Server.getCommandResult(gameId).getPlayerState().getLocation())
        assertEquals(initialDirection, Server.getCommandResult(gameId).getPlayerState().getDirection())
        assertEquals(initialInventory, Server.getCommandResult(gameId).getPlayerState().getInventory())
    }

    @Test
    fun `check player turns on turn command`() {
        assertEquals(initialDirection, Server.getCommandResult(gameId).getPlayerState().getDirection())
        Server.makeMove(gameId, TurnCommand(Direction.EAST))
        assertEquals(initialPoint, Server.getCommandResult(gameId).getPlayerState().getLocation())
        assertEquals(Direction.EAST, Server.getCommandResult(gameId).getPlayerState().getDirection())
        assertEquals(initialInventory, Server.getCommandResult(gameId).getPlayerState().getInventory())
    }

    @Test
    fun `check player grabs on grab command`() {
        Server.getGame(gameId).addToRoom(initialPoint, GameObject.FOOD)
        assertEquals(initialInventory, Server.getCommandResult(gameId).getPlayerState().getInventory())
        Server.makeMove(gameId, GrabCommand(InventoryItem.FOOD))
        assertEquals(initialPoint, Server.getCommandResult(gameId).getPlayerState().getLocation())
        assertEquals(initialDirection, Server.getCommandResult(gameId).getPlayerState().getDirection())
        assertEquals(initialInventory + mapOf(InventoryItem.FOOD to 1), Server.getCommandResult(gameId).getPlayerState().getInventory())
    }

    @Test
    fun `get command result`() {
        val sessionId = Helpers.createServerSession(Helpers.testFilePath + "testFile.json")
        Server.makeMove(sessionId, MoveCommand())
        assertEquals(arrayListOf(Perception.BREEZE).toString(), Server.getCommandResult(sessionId).getPerceptions().toString())
    }

    @Test
    fun `check for mandatory world properties on create without file`() {
        val game = Server.createGame("", 10)
        checkWorldProperties(game)
    }

    @Test
    fun `check for mandatory world properties on create with file`() {
        val game = Server.createGame(Helpers.testFilePath + "testFile.json", 10)
        checkWorldProperties(game)
    }

    private fun checkWorldProperties(game: Game) {
        assertTrue(game.hasGameObject(game.getPlayerLocation(), GameObject.EXIT))
        assertEquals(1, game.getAmountOfObjectsInRoom(game.getPlayerLocation()))
    }
}

data class ValidPostCommandPlayerTestData (
    val givenServer: Server,
    val command: Command,
    val expectedPlayerLocation: Point,
    val expectedPlayerDirection: Direction,
    val expectedPlayerInventory: PlayerInventory
)
