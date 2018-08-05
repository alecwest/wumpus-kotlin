package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.command.TurnCommand
import game.world.Perception
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.io.ByteArrayInputStream
import java.util.stream.Stream

internal class IntelligenceTest {

    @ParameterizedTest
    @MethodSource("validBasicIntelligenceTestDataProvider")
    fun `choose next move with basic intelligence`(testData: ValidIntelligenceTestData) {
        assertEquals(testData.expectedCommand, BasicIntelligence().chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    @Test
    fun `process last move with base method`() {
        val lastMove = Helpers.createCommandResult(arrayListOf(),
                Helpers.createPlayerState(location = Point(4, 4)),
                arrayListOf(RoomContent.BREEZE))
        val intelligence = BasicIntelligence()

        intelligence.processLastMove(world, lastMove)
        assertTrue(world.hasRoomContent(lastMove.getPlayerState().getLocation(), RoomContent.BREEZE))
    }

    companion object {
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 4) to arrayListOf(RoomContent.BLOCKADE)))
        val commandResult = CommandResult()

        @JvmStatic
        fun validBasicIntelligenceTestDataProvider() = Stream.of(
                ValidIntelligenceTestData(world, commandResult, MoveCommand()),
                ValidIntelligenceTestData(world, commandResult.copyThis(
                        perceptions = arrayListOf(Perception.BLOCKADE_BUMP),
                        playerState = Helpers.createPlayerState(location = Point(0, 3))),
                        TurnCommand(Direction.EAST))
        )
    }
}

data class ValidIntelligenceTestData (
    val givenWorld: World,
    val givenCommandResult: CommandResult,
    val expectedCommand: Command
)