package game

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class GameTest {
    @ParameterizedTest
    @MethodSource("validGameTestDataProvider")
    fun `check game is over`(testData: ValidGameTestData) {
        assertEquals(testData.expectedGameOver, testData.givenGame.gameOver())
    }

    companion object {
        @JvmStatic
        fun validGameTestDataProvider() = Stream.of(
                ValidGameTestData(Game(), false),
                ValidGameTestData(Game(GameState(active = false)), true)
        )
    }
}

data class ValidGameTestData (
    val givenGame: Game,
    val expectedGameOver: Boolean
)