package server.command

import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

internal class TurnCommandTest {
    @ParameterizedTest
    @MethodSource("validTurnCommandTestDataProvider")
    fun `execute turn commands`(testData: ValidTurnCommandTestData) {
        testData.command.execute()
        assertEquals(testData.expectedDirection, initialGame.getPlayerDirection())
        // Verify the rest of the player state is maintained
        assertEquals(Point(2, 2), initialGame.getPlayerLocation())
    }

    companion object {
        private val initialGame = Game(GameState(player =
        Player(playerState =
        PlayerState(location = Point(2, 2), facing = Direction.SOUTH))))

        @JvmStatic
        fun validTurnCommandTestDataProvider() = Stream.of(
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.EAST), Direction.EAST),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.NORTH), Direction.NORTH),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.EAST), Direction.EAST),
                ValidTurnCommandTestData(initialGame, TurnCommand(initialGame, Direction.SOUTH), Direction.SOUTH)
        )
    }
}

data class ValidTurnCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedDirection: Direction
)