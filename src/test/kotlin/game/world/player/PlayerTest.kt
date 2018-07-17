package game.world.player

import game.player.Inventory
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream
import kotlin.test.assertEquals

class PlayerTest {
    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player is alive`(testData: ValidPlayerTestData) {
        assertEquals(testData.expectedAlive, testData.givenPlayer.isAlive())
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player location`(testData: ValidPlayerTestData) {
        assertEquals(testData.expectedLocation, testData.givenPlayer.getLocation())
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player has inventory`(testData: ValidPlayerTestData) {
        assertEquals(testData.hasArrow, testData.givenPlayer.hasItem(Inventory.ARROW))
        assertEquals(testData.hasFood, testData.givenPlayer.hasItem(Inventory.FOOD))
        assertEquals(testData.hasGold, testData.givenPlayer.hasItem(Inventory.GOLD))
    }

    @ParameterizedTest
    @MethodSource("validPlayerTestDataProvider")
    fun `check player number items in inventory`(testData: ValidPlayerTestData) {
        assertEquals(testData.numArrow, testData.givenPlayer.getNumberOf(Inventory.ARROW))
        assertEquals(testData.numFood, testData.givenPlayer.getNumberOf(Inventory.FOOD))
        assertEquals(testData.numGold, testData.givenPlayer.getNumberOf(Inventory.GOLD))
    }

    companion object {
        @JvmStatic
        fun validPlayerTestDataProvider() = Stream.of(
                ValidPlayerTestData(Player(PlayerState(alive = true, location = Point(1, 3),
                        inventory = PlayerInventory(mapOf(Inventory.ARROW to 1,
                                Inventory.FOOD to 2, Inventory.GOLD to 3)))),
                        expectedAlive = true, expectedLocation = Point(1, 3),
                        hasArrow = true, hasFood = true, hasGold = true,
                        numArrow = 1, numFood = 2, numGold = 3),
                ValidPlayerTestData(Player(PlayerState(alive = false, location = Point(4, 6),
                        inventory = PlayerInventory(mapOf()))),
                        expectedAlive = false, expectedLocation = Point(4, 6),
                        hasArrow = false, hasFood = false, hasGold = false,
                        numArrow = 0, numFood = 0, numGold = 0)
        )
    }
}

data class ValidPlayerTestData (
    val givenPlayer: Player,
    val expectedAlive: Boolean,
    val expectedLocation: Point,
    val hasArrow: Boolean,
    val hasFood: Boolean,
    val hasGold: Boolean,
    val numArrow: Int,
    val numFood: Int,
    val numGold: Int
)