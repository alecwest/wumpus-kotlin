package game.agent

import game.agent.intelligence.BasicIntelligence
import game.client.Client
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
import java.util.stream.Stream

internal class AgentTest {
    private val client = Client(worldSize = 5)
    private val agent = Agent(client, BasicIntelligence())

    @Test
    fun `initialize client`() {
        assertEquals(5, agent.client.getWorldSize())
    }

    @Test
    fun `initialize agent world`() {
        assertEquals(5, agent.world.getSize())
        assertEquals(0, agent.world.getRooms().filter { !it.isEmpty() }.size)
    }

    @ParameterizedTest
    @MethodSource("validBasicIntelligenceAgentTestDataProvider")
    fun `choose next move with basic intelligence`(testData: ValidAgentTestData) {
        assertEquals(testData.expectedCommand, BasicIntelligence().chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    companion object {
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 4) to arrayListOf(RoomContent.BLOCKADE)))
        val commandResult = CommandResult()

        @JvmStatic
        fun validBasicIntelligenceAgentTestDataProvider() = Stream.of(
                ValidAgentTestData(world, commandResult, MoveCommand()),
                ValidAgentTestData(world, commandResult.copyThis(
                        perceptions = arrayListOf(Perception.BLOCKADE_BUMP),
                        playerState = Helpers.createPlayerState(location = Point(0, 3))),
                        TurnCommand(Direction.EAST))
        )
    }
}

data class ValidAgentTestData (
        val givenWorld: World,
        val givenCommandResult: CommandResult,
        val expectedCommand: Command
)