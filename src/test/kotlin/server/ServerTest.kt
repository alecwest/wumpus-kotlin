package server

import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import server.command.*
import util.*
import java.awt.Point

class ServerTest {
    private val server = Server()
    private val initialPoint = Point(0, 0)
    private val initialDirection = Direction.NORTH
    private val initialInventory = mapOf(InventoryItem.ARROW to 1)

    @Test
    fun `server generates world from world size only`() {
        val worldSize = 10
        val server = Server(worldSize = worldSize)

        assertEquals(worldSize, server.getGame().getWorldSize())
        assertEquals(worldSize * worldSize, server.getGame().getNumberRooms())
    }

    @Test
    fun `server generates world from json file`() {
        val fileName = "src/test/resources/testFile.json"
        val server = Server(fileName = fileName)

        assertEquals(11, server.getGame().getWorldSize())
        assertEquals(121, server.getGame().getNumberRooms())
    }

    @Test
    fun `get room content`() {
        assertEquals(arrayListOf<RoomContent>(), server.getRoomContent())
        server.getGame().addToRoom(initialPoint, RoomContent.FOOD)
        assertEquals(arrayListOf(RoomContent.FOOD), server.getRoomContent())
    }

    // TODO add bad move tests

    @Test
    fun `check player moves on move command`() {
        assertEquals(initialPoint, server.getPlayerState().getLocation())
        server.makeMove(MoveCommand(server.getGame(), server.getPlayerState().getDirection()))
        assertEquals(initialPoint.north(), server.getPlayerState().getLocation())
        assertEquals(initialDirection, server.getPlayerState().getDirection())
        assertEquals(initialInventory, server.getPlayerState().getInventory())
    }

    @Test
    fun `check player turns on turn command`() {
        assertEquals(initialDirection, server.getPlayerState().getDirection())
        server.makeMove(TurnCommand(server.getGame(), Direction.EAST))
        assertEquals(initialPoint, server.getPlayerState().getLocation())
        assertEquals(Direction.EAST, server.getPlayerState().getDirection())
        assertEquals(initialInventory, server.getPlayerState().getInventory())
    }

    @Test
    fun `check player grabs on grab command`() {
        server.getGame().addToRoom(initialPoint, RoomContent.FOOD)
        assertEquals(initialInventory, server.getPlayerState().getInventory())
        server.makeMove(GrabCommand(server.getGame(), InventoryItem.FOOD))
        assertEquals(initialPoint, server.getPlayerState().getLocation())
        assertEquals(initialDirection, server.getPlayerState().getDirection())
        assertEquals(initialInventory + mapOf(InventoryItem.FOOD to 1), server.getPlayerState().getInventory())
    }
}

data class ValidPostCommandPlayerTestData (
    val givenServer: Server,
    val command: Command,
    val expectedPlayerLocation: Point,
    val expectedPlayerDirection: Direction,
    val expectedPlayerInventory: PlayerInventory
)
