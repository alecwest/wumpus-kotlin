package game

import Helpers.Companion.createGame
import Helpers.Companion.createPlayer
import Helpers.Companion.createWorld
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.*
import java.awt.Point
import java.util.stream.Stream

class GameTest {
    val game = createGame()
    val player = createPlayer()
    val world = createWorld()
    val pitRoomPoint = Point(2, 2)

    @ParameterizedTest
    @MethodSource("validGameTestDataProvider")
    fun `check game is over`(testData: ValidGameTestData) {
        assertEquals(testData.expectedGameOver, testData.givenGame.gameOver())
        assertEquals(!testData.expectedGameOver, testData.givenGame.getActive())
    }

    companion object {
        @JvmStatic
        fun validGameTestDataProvider() = Stream.of(
                ValidGameTestData(Game(), false),
                ValidGameTestData(Game(GameState(active = false)), true)
        )
    }

    @Test
    fun `check given world is equal on get`() {
        val game = createGame(world = world)
        assertEquals(world, game.getWorld())
    }

    @Test
    fun `check given rooms are equal on get`() {
        val game = createGame(world = world)
        assertEquals(world.getRooms(), game.getRooms())
    }

    @Test
    fun `check room has content`() {
        assertTrue(game.hasRoomContent(pitRoomPoint, RoomContent.PIT))
        assertTrue(game.hasRoomContent(pitRoomPoint.north(), RoomContent.BREEZE))
        assertTrue(game.hasRoomContent(pitRoomPoint.east(), RoomContent.BREEZE))
        assertTrue(game.hasRoomContent(pitRoomPoint.south(), RoomContent.BREEZE))
        assertTrue(game.hasRoomContent(pitRoomPoint.west(), RoomContent.BREEZE))
    }

    @Test
    fun `check room is empty`() {
        assertTrue(game.roomIsEmpty(Point(0, 0)))
        assertFalse(game.roomIsEmpty(pitRoomPoint))
    }

    @Test
    fun `check room index`() {
        assertEquals(22, game.getRoomIndex(pitRoomPoint))
    }

    @Test
    fun `check world map is equal on get`() {
        val game = createGame(world = world)
        assertEquals(world.getWorldMap(), game.getWorldMap())
    }

    @Test
    fun `check room is equal on get`() {
        val game = createGame(world = world)
        assertEquals(world.getRoom(pitRoomPoint), game.getRoom(pitRoomPoint))
    }

    @Test
    fun `check number of rooms`() {
        val game2 = createGame(world = createWorld(5))
        assertEquals(100, game.getNumberRooms())
        assertEquals(25, game2.getNumberRooms())
    }

    @Test
    fun `check amount of content in room`() {
        assertEquals(1, game.getAmountOfContentInRoom(pitRoomPoint))
        assertEquals(0, game.getAmountOfContentInRoom(Point(0, 0)))
    }

    @Test
    fun `check player is equal on get`() {
        val game = createGame(player = player)
        assertEquals(player, game.getPlayer())
    }

    @Test
    fun `check player is alive`() {
        val player = createPlayer(alive = false)
        assertTrue(game.isPlayerAlive())
        val game = createGame(player = player)
        assertFalse(game.isPlayerAlive())
    }

    @Test
    fun `check player location is equal on get`() {
        val game = createGame(player = player)
        assertEquals(player.getLocation(), game.getPlayerLocation())
    }

    @Test
    fun `check player direction is equal on get`() {
        val game = createGame(player = player)
        assertEquals(player.getDirection(), game.getPlayerDirection())
    }

    @Test
    fun `check player inventory is equal on get`() {
        val game = createGame(player = player)
        assertEquals(player.getInventory(), game.getPlayerInventory())
    }

    @Test
    fun `add to player inventory`() {
        game.addToPlayerInventory(InventoryItem.FOOD)
        assertEquals(1, game.getPlayerInventory().getValue(InventoryItem.FOOD))
    }

    @Test
    fun `remove from player inventory`() {
        game.removeFromPlayerInventory(InventoryItem.ARROW)
        assertEquals(1, game.getPlayerInventory().getValue(InventoryItem.ARROW))
    }

    @Test
    fun `check alive state of player on change`() {
        assertTrue(game.isPlayerAlive())
        game.setPlayerAlive(false)
        assertFalse(game.isPlayerAlive())
    }

    @Test
    fun `check location of player on change`() {
        assertEquals(Point(0,0), game.getPlayerLocation())
        game.setPlayerLocation(Point(3, 6))
        assertEquals(Point(3, 6), game.getPlayerLocation())
    }

    @Test
    fun `check direction player is facing on change`() {
        assertEquals(Direction.NORTH, game.getPlayerDirection())
        game.setPlayerDirection(Direction.SOUTH)
        assertEquals(Direction.SOUTH, game.getPlayerDirection())
    }

    @Test
    fun `check inventory of player on change`() {
        val newInventory = mapOf(InventoryItem.GOLD to 2)
        assertEquals(mapOf(InventoryItem.ARROW to 2), game.getPlayerInventory())
        game.setPlayerInventory(PlayerInventory(newInventory))
        assertEquals(newInventory, game.getPlayerInventory())
    }
}

data class ValidGameTestData (
    val givenGame: Game,
    val expectedGameOver: Boolean
)