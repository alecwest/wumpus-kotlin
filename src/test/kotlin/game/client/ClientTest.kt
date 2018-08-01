package game.client

import game.command.*
import game.server.Server
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.Direction

internal class ClientTest {
    private val worldFileName = "src/test/resources/testFile.json"

    @Test
    fun `initialize Client session`() {
        assertEquals(0, Client().sessionId)
        assertEquals(1, Client().sessionId)
    }

    @Test
    fun `make move`() {
        val client = Client()
        client.makeMove(TurnCommand(Direction.WEST))
        assertEquals(Direction.WEST, Server.getGame(client.sessionId).getPlayerDirection())
    }

    @Test
    fun `get move result`() {
        val client = Client(worldFileName)
        val moveCommand = MoveCommand()
        moveCommand.setGame(Server.getGame(client.sessionId))
        moveCommand.execute()

        assertEquals(arrayListOf(RoomContent.BREEZE).toString(), client.getMoveResult()?.getPerceptions().toString())
    }
}