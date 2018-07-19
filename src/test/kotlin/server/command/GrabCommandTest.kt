package server.command

import game.Game
import game.GameState
import game.player.Inventory
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

class GrabCommandTest {
    @ParameterizedTest
    @MethodSource("validGrabCommandTestDataProvider")
    fun `execute grab command`(testData: ValidGrabCommandTestData) {
        testData.command.execute()
        Assertions.assertEquals(testData.expectedInventory, initialGame.gameState.player.getInventory())
        // Verify the rest of the player state is maintained
        Assertions.assertEquals(Direction.SOUTH, initialGame.gameState.player.getDirection())
    }

    companion object {
        private val initialGame = Game(GameState(player =
        Player(playerState =
        PlayerState(location = Point(2, 2), facing = Direction.SOUTH,
                inventory = PlayerInventory(mapOf(Inventory.ARROW to 2))))))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validGrabCommandTestDataProvider() = Stream.of(
                ValidGrabCommandTestData(initialGame, GrabFoodCommand(initialGame), PlayerInventory(mapOf(Inventory.ARROW to 2, Inventory.FOOD to 1))),
                ValidGrabCommandTestData(initialGame, GrabFoodCommand(initialGame), PlayerInventory(mapOf(Inventory.ARROW to 2, Inventory.FOOD to 2)))
        )
    }
}

data class ValidGrabCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedInventory: PlayerInventory
)