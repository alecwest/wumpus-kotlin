package game

import Helpers.createGame
import Helpers.createPlayer
import Helpers.createWorld
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.GameObject
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.*
import java.awt.Point
import java.util.stream.Stream

class GameTest {
    private val game = createGame(world = createWorld(gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT))))
    private val player = createPlayer()
    private val world = createWorld(gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT)))
    private val pitRoomPoint = Point(2, 2)

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
    fun `get game state`() {
        assertEquals(GameState(
                player = Helpers.createPlayer(
                        inventoryContent = mapOf(InventoryItem.ARROW to 2))),
                game.getGameState())
    }

    @Test
    fun `check given world is equal on get`() {
        val game = createGame(world = world)
        assertEquals(world, game.getWorld())
    }

    @Test
    fun `check given world size is equal on get`() {
        val world = createWorld(size = 5)
        val game = createGame(world = world)
        assertEquals(world.getSize(), game.getWorldSize())
    }

    @Test
    fun `check given rooms are equal on get`() {
        val game = createGame(world = world)
        assertEquals(world.getRooms(), game.getRooms())
    }

    @Test
    fun `get room content`() {
        assertEquals(setOf<GameObject>(), world.getGameObjects(Point(0, 0)))
        assertEquals(setOf(GameObject.PIT), world.getGameObjects(Point(2, 2)))
    }

    @Test
    fun `check room has content`() {
        assertTrue(game.hasGameObject(pitRoomPoint, GameObject.PIT))
        assertTrue(game.hasGameObject(pitRoomPoint.north(), GameObject.BREEZE))
        assertTrue(game.hasGameObject(pitRoomPoint.east(), GameObject.BREEZE))
        assertTrue(game.hasGameObject(pitRoomPoint.south(), GameObject.BREEZE))
        assertTrue(game.hasGameObject(pitRoomPoint.west(), GameObject.BREEZE))
    }

    @Test
    fun `check room is valid`() {
        assertTrue(game.roomIsValid(Point(0, 0)))
        assertFalse(game.roomIsValid(Point(-1, 0)))
        assertFalse(game.roomIsValid(Point(0, 11)))
        assertFalse(game.roomIsValid(Point(10, 4)))
    }

    @Test
    fun `check room is empty`() {
        assertTrue(game.roomIsEmpty(Point(0, 1)))
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
        assertEquals(1, game.getAmountOfObjectsInRoom(pitRoomPoint))
        assertEquals(0, game.getAmountOfObjectsInRoom(Point(0, 1)))
    }

    @Test
    fun `add content to room`() {
        val world = createWorld()
        val game = createGame(world = world)
        assertFalse(world.hasGameObject(pitRoomPoint, GameObject.GOLD))
        game.addToRoom(pitRoomPoint, GameObject.GOLD)
        assertTrue(world.hasGameObject(pitRoomPoint, GameObject.GOLD))
    }

    @Test
    fun `remove content from room`() {
        val world = createWorld(gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT)))
        val game = createGame(world = world)
        assertTrue(world.hasGameObject(pitRoomPoint, GameObject.PIT))
        game.removeFromRoom(pitRoomPoint, GameObject.PIT)
        assertFalse(world.hasGameObject(pitRoomPoint, GameObject.PIT))
    }

    @Test
    fun `check player is equal on get`() {
        val game = createGame(player = player)
        assertEquals(player, game.getPlayer())
    }

    @Test
    fun `get player state`() {
        val game = createGame(player = player)
        assertEquals(player.getPlayerState(), game.getPlayerState())
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
    fun `player has item`() {
        assertTrue(game.playerHasItem(InventoryItem.ARROW))
        assertFalse(game.playerHasItem(InventoryItem.FOOD))
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
    fun `set player location to dangerous room`() {
        assertEquals(true, game.isPlayerAlive())
        game.setPlayerLocation(Point(2, 2))
        assertEquals(false, game.isPlayerAlive())
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

    @Test
    fun `check score of player on change`() {
        val newScore = 16
        assertEquals(0, game.getScore())
        game.setPlayerScore(newScore)
        assertEquals(newScore, game.getScore())
    }

    @Test
    fun `check game active on change`() {
        assertEquals(true, game.getActive())
        game.setActive(false)
        assertEquals(false, game.getActive())
    }

    @Test
    fun `check exit is added to player starting location`() {
        val gameWithDifferentStart = Helpers.createGame(player = Helpers.createPlayer(location = Point(4, 4)))
        assertEquals(setOf(GameObject.EXIT), game.getGameObjects(game.getPlayerLocation()))
        assertEquals(setOf(GameObject.EXIT),
                gameWithDifferentStart.getGameObjects(gameWithDifferentStart.getPlayerLocation()))
        assertEquals(1, game.getRooms().count { it.hasGameObject(GameObject.EXIT) })
        assertEquals(1, gameWithDifferentStart.getRooms().count { it.hasGameObject(GameObject.EXIT) })
        assertTrue(game.getCommandResult().getPerceptions().contains(Perception.EXIT))
    }

    @Test
    fun `walk into a room with a friendly supmuw`() {
        val game = Helpers.createGame(world = Helpers.createWorld(
                gameObject = mapOf(Point(0, 1) to setOf(GameObject.SUPMUW))))
        game.setPlayerLocation(Point(0, 1))
        assertTrue(game.isPlayerAlive())
        assertTrue(game.hasGameObject(Point(0, 1), GameObject.FOOD))
    }

    @Test
    fun `walk into a room with a dangerous supmuw`() {
        val game = Helpers.createGame(world = Helpers.createWorld(
                gameObject = mapOf(Point(0, 1) to setOf(GameObject.SUPMUW),
                        Point(0, 2) to setOf(GameObject.WUMPUS))))
        game.setPlayerLocation(Point(0, 1))
        assertFalse(game.isPlayerAlive())
        assertFalse(game.hasGameObject(Point(0, 1), GameObject.FOOD))
    }

    @Test
    fun `remove conditional effect after adding an object that the proximity condition wants none of`() {
        for (i in 0..2) for (j in 0..2) {
            game.addToRoom(Point(i, j), GameObject.SUPMUW)
            assertTrue(game.hasGameObject(Point(i, j), GameObject.FOOD))
        }

        // TODO remove this after passing
        game.getGameObjects(Point(1, 1)).forEach {
            game.removeFromRoom(Point(1, 1), it)
        }

        game.addToRoom(Point(1,1), GameObject.WUMPUS)

        for (i in 0..2) for (j in 0..2) {
            assertFalse(game.hasGameObject(Point(i, j), GameObject.FOOD))
        }
    }
}

data class ValidGameTestData (
    val givenGame: Game,
    val expectedGameOver: Boolean
)