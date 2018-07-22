package game.player

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream
import kotlin.test.assertEquals

class PlayerTest {
    private val player = Player()

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player is alive`(testData: ValidPlayerTestData) {
        assertEquals(testData.expectedAlive, testData.givenPlayer.isAlive())
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player location`(testData: ValidPlayerTestData) {
        assertEquals(testData.expectedLocation, testData.givenPlayer.getLocation())
        assertEquals(testData.expectedDirection, testData.givenPlayer.getDirection())
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player has inventory`(testData: ValidPlayerTestData) {
        assertEquals(testData.hasArrow, testData.givenPlayer.hasItem(InventoryItem.ARROW))
        assertEquals(testData.hasFood, testData.givenPlayer.hasItem(InventoryItem.FOOD))
        assertEquals(testData.hasGold, testData.givenPlayer.hasItem(InventoryItem.GOLD))
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player number items in inventory`(testData: ValidPlayerTestData) {
        assertEquals(testData.numArrow, testData.givenPlayer.getNumberOf(InventoryItem.ARROW))
        assertEquals(testData.numFood, testData.givenPlayer.getNumberOf(InventoryItem.FOOD))
        assertEquals(testData.numGold, testData.givenPlayer.getNumberOf(InventoryItem.GOLD))
    }

    companion object {
        @JvmStatic
        fun validPlayerTestDataProvider() = Stream.of(
                ValidPlayerTestData(Player(PlayerState(alive = true,
                        location = Point(1, 3), facing = Direction.NORTH,
                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 1,
                                InventoryItem.FOOD to 2, InventoryItem.GOLD to 3)))),
                        expectedAlive = true, expectedLocation = Point(1, 3),
                        expectedDirection = Direction.NORTH,
                        hasArrow = true, hasFood = true, hasGold = true,
                        numArrow = 1, numFood = 2, numGold = 3),
                ValidPlayerTestData(Player(PlayerState(alive = false,
                        location = Point(4, 6), facing = Direction.WEST,
                        inventory = PlayerInventory(mapOf()))),
                        expectedAlive = false, expectedLocation = Point(4, 6),
                        expectedDirection = Direction.WEST,
                        hasArrow = false, hasFood = false, hasGold = false,
                        numArrow = 0, numFood = 0, numGold = 0),
                ValidPlayerTestData(Player(),
                        expectedAlive = true, expectedLocation = Point(0, 0),
                        expectedDirection = Direction.NORTH,
                        hasArrow = true, hasFood = false, hasGold = false,
                        numArrow = 1, numFood = 0, numGold = 0)
        )
    }

    @Test
    fun `check player adds to inventory`() {
        assertEquals(1, player.getNumberOf(InventoryItem.ARROW))
        player.addToInventory(InventoryItem.ARROW)
        assertEquals(2, player.getNumberOf(InventoryItem.ARROW))
        assertEquals(0, player.getNumberOf(InventoryItem.FOOD))
        player.addToInventory(InventoryItem.FOOD)
        assertEquals(1, player.getNumberOf(InventoryItem.FOOD))
    }

    @Test
    fun `check player removes from inventory`() {
        assertEquals(1, player.getNumberOf(InventoryItem.ARROW))
        player.removeFromInventory(InventoryItem.ARROW)
        assertEquals(0, player.getNumberOf(InventoryItem.ARROW))
        player.removeFromInventory(InventoryItem.FOOD)
        assertEquals(0, player.getNumberOf(InventoryItem.FOOD))
    }

    @Test
    fun `check player dead`() {
        player.setAlive(false)
        assertEquals(false, player.isAlive())
    }

    @Test
    fun `check player location`() {
        player.setLocation(Point(2, 3))
        assertEquals(Point(2, 3), player.getLocation())
    }

    @Test
    fun `check player facing`() {
        player.setFacing(Direction.WEST)
        assertEquals(Direction.WEST, player.getDirection())
    }

    @Test
    fun `check player inventory`() {
        player.setInventory(PlayerInventory(mapOf(InventoryItem.GOLD to 12)))
        assertEquals(mapOf(InventoryItem.GOLD to 12), player.getInventory())
    }
}

data class ValidPlayerTestData (
    val givenPlayer: Player,
    val expectedAlive: Boolean,
    val expectedLocation: Point,
    val expectedDirection: Direction,
    val hasArrow: Boolean,
    val hasFood: Boolean,
    val hasGold: Boolean,
    val numArrow: Int,
    val numFood: Int,
    val numGold: Int
)