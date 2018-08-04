package game.agent.intelligence

import Helpers.Companion.assertCommandEquals
import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

internal class IntelligenceTest {

    @ParameterizedTest
    @MethodSource("validIntelligenceTestDataProvider")
    fun `use basic intelligence`(testData: ValidIntelligenceTestData) {
        assertCommandEquals(MoveCommand(), testData.givenIntelligence.chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    companion object {
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 4) to arrayListOf(RoomContent.BLOCKADE)))
        val commandResult = CommandResult()

        @JvmStatic
        fun validIntelligenceTestDataProvider() = Stream.of(
                ValidIntelligenceTestData(world, BasicIntelligence(), commandResult, MoveCommand())
        )
    }
}

data class ValidIntelligenceTestData (
    val givenWorld: World,
    val givenIntelligence: Intelligence,
    val givenCommandResult: CommandResult,
    val expectedCommand: Command
)