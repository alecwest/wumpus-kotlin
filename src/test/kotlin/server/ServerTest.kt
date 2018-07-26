package server

import game.player.InventoryItem
import game.player.PlayerInventory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import server.command.Command
import server.command.turn.*
import server.command.move.*
import server.command.grab.*
import util.Direction
import util.left
import util.north
import java.awt.Point
import java.util.stream.Stream

class ServerTest {
    @Test
    fun `server generates world from world size only`() {
        val worldSize = 10
        val server = Server(worldSize = worldSize)

        assertEquals(worldSize, server.getWorldSize())
        assertEquals(worldSize * worldSize, server.getNumberRooms())
    }

    @Test
    fun `server generates world from json file`() {
        val fileName = "src/test/resources/testFile.json"
        val server = Server(fileName = fileName)

        assertEquals(11, server.getWorldSize())
        assertEquals(121, server.getNumberRooms())
    }

    @Test
    fun `check number of rooms`() {
        val server = Server()
        val server2 = Server(worldSize = 5)
        assertEquals(100, server.getNumberRooms())
        assertEquals(25, server2.getNumberRooms())
    }

    companion object {
        private val server = Server()
        private val initialPoint = Point(0, 0)
        private val initialDirection = Direction.NORTH
        private val initialInventory = mapOf(InventoryItem.ARROW to 1)

        @JvmStatic
        fun validPostCommandPlayerTestDataProvider() = Stream.of(
            ValidPostCommandPlayerTestData(server, TurnCommand(server.getPlayerState().getDirection().left()), initialPoint, initialDirection.left(), PlayerInventory(initialInventory)),
            ValidPostCommandPlayerTestData(server, MoveCommand(server.getPlayerState().getDirection()), initialPoint.north(), initialDirection, PlayerInventory(initialInventory)),
            ValidPostCommandPlayerTestData(server, GrabCommand(InventoryItem.GOLD), initialPoint, initialDirection, PlayerInventory(initialInventory + mapOf(InventoryItem.FOOD to 1)))
        )
        // TODO add get player and get world function calls to Server?
    }

    @ParameterizedTest
    @MethodSource("validPostCommandPlayerTestDataProvider")
    fun `check player is modified on command execution`(testData: ValidPostCommandPlayerTestData) {
        testData.givenServer.makeMove(testData.command)
        assertEquals(testData.expectedPlayerLocation, testData.givenServer.getPlayerState().getLocation())
        assertEquals(testData.expectedPlayerDirection, testData.givenServer.getPlayerState().getDirection())
        assertEquals(testData.expectedPlayerInventory, testData.givenServer.getPlayerState().getInventory())
    }
}

data class ValidPostCommandPlayerTestData (
    val givenServer: Server,
    val command: Command,
    val expectedPlayerLocation: Point,
    val expectedPlayerDirection: Direction,
    val expectedPlayerInventory: PlayerInventory
)
