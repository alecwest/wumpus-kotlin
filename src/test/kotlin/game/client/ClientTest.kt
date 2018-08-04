package game.client

import game.command.*
import game.player.InventoryItem
import game.server.Server
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.Direction

internal class ClientTest {
    @Test
    fun `initialize Client session`() {
        val client = Client()
        assertEquals(client.sessionId + 1, Client().sessionId)
    }

    @Test
    fun `get world size`() {
        assertEquals(5, Client(worldSize = 5).getWorldSize())
    }

    @Test
    fun `make move`() {
        val client = Client()
        client.makeMove(TurnCommand(Direction.WEST))
        assertEquals(Direction.WEST, Server.getGame(client.sessionId).getPlayerDirection())
    }

    @Test
    fun `get move result`() {
        val client = Client(Helpers.worldFileName)
        val moveCommand = MoveCommand()
        moveCommand.setGame(Server.getGame(client.sessionId))
        moveCommand.execute()

        assertEquals(arrayListOf(RoomContent.BREEZE).toString(), client.getMoveResult().getRoomContent().toString())
    }
}