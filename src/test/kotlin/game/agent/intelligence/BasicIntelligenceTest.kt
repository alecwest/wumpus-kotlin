package game.agent.intelligence

import game.command.*
import game.world.Perception
import game.world.World
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

class BasicIntelligenceTest {
    @ParameterizedTest
    @MethodSource("validBasicIntelligenceTestDataProvider")
    fun `choose next move with basic intelligence`(testData: ValidBasicIntelligenceTestData) {
        Assertions.assertEquals(testData.expectedCommand, BasicIntelligence().chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    companion object {
        @JvmStatic
        fun validBasicIntelligenceTestDataProvider() = Stream.of(
                ValidBasicIntelligenceTestData(IntelligenceTest.world, IntelligenceTest.commandResult, MoveCommand()),
                ValidBasicIntelligenceTestData(IntelligenceTest.world, IntelligenceTest.commandResult.copyThis(
                        perceptions = arrayListOf(Perception.BLOCKADE_BUMP),
                        playerState = Helpers.createPlayerState(location = Point(0, 3))),
                        TurnCommand(Direction.EAST))
        )
    }
}

data class ValidBasicIntelligenceTestData (
        val givenWorld: World,
        val givenCommandResult: CommandResult,
        val expectedCommand: Command
)