package server.command


import game.Game
import game.GameState
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import util.right
import java.awt.Point
import java.util.stream.Stream

internal class MoveCommandTest {

    @ParameterizedTest
    @MethodSource("validMoveCommandTestDataProvider")
    fun `execute move commands`(testData: ValidMoveCommandTestData) {
        testData.command.execute()
        assertEquals(testData.expectedPoint, testData.givenGame.getPlayerLocation())
        TurnCommand(testData.givenGame, testData.givenGame.getPlayerDirection().right()).execute()
        // Verify the rest of the player state is maintained
        assertEquals(mapOf(InventoryItem.ARROW to 2), testData.givenGame.getPlayerInventory())
    }

    companion object {
        private val initialGame = Helpers.createGame(player = Helpers.createPlayer(
                location = Point(2, 2), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2)))
        private val playerInCornerGame = Helpers.createGame(player = Helpers.createPlayer(
                location = Point(0, 0), facing = Direction.SOUTH, inventoryContent = mapOf(InventoryItem.ARROW to 2)))

        // TODO initialGame is not initialized at the start of every test, so these must run in succession to pass
        @JvmStatic
        fun validMoveCommandTestDataProvider() = Stream.of(
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame), Point(2, 1)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame), Point(1, 1)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame), Point(1, 2)),
            ValidMoveCommandTestData(initialGame, MoveCommand(initialGame), Point(2, 2)),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(playerInCornerGame), Point(0, 0)),
            ValidMoveCommandTestData(playerInCornerGame, MoveCommand(playerInCornerGame), Point(0, 0))
        )
    }
}

data class ValidMoveCommandTestData (
    val givenGame: Game,
    val command: Command,
    val expectedPoint: Point
)