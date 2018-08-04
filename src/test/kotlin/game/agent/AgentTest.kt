package game.agent

import game.agent.intelligence.BasicIntelligence
import game.client.Client
import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.command.TurnCommand
import game.player.InventoryItem
import game.server.Server
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
        assertEquals(testData.expectedCommand, basicIntelligenceAgent.chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    @ParameterizedTest
    @MethodSource("validPostMoveBasicIntelligenceAgentTestDataProvider")
    fun `make next move with basic intelligence`(testData: ValidPostMoveAgentTestData) {
        basicIntelligenceAgent.makeNextMove()
        assertEquals(testData.expectedCommandResult, basicIntelligenceAgent.client.getMoveResult())
    }

    companion object {
        val basicIntelligenceClient = Helpers.createClient(Helpers.basicIntelligenceWorldFileName)
        val basicIntelligenceAgent = Helpers.createAgent(basicIntelligenceClient, BasicIntelligence())
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 1) to arrayListOf(RoomContent.BLOCKADE)))
        val commandResult = Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(
                        inventoryContent = mapOf(InventoryItem.ARROW to 1)
                )
        )

        @JvmStatic
        fun validBasicIntelligenceAgentTestDataProvider() = Stream.of(
                ValidAgentTestData(world, commandResult, MoveCommand()),
                ValidAgentTestData(world, commandResult.copyThis(
                        perceptions = arrayListOf(Perception.BLOCKADE_BUMP),
                        playerState = commandResult.getPlayerState().copyThis(location = Point(0, 0))),
                        TurnCommand(Direction.EAST))
        )

        @JvmStatic
        fun validPostMoveBasicIntelligenceAgentTestDataProvider() = Stream.of(
                ValidPostMoveAgentTestData(world,
                        commandResult.copyThis(
                                playerState = commandResult.getPlayerState().copyThis(location = Point(0, 3))
                        )),
                ValidPostMoveAgentTestData(world,
                        commandResult.copyThis(
                                perceptions = arrayListOf(Perception.BLOCKADE_BUMP),
                                playerState = commandResult.getPlayerState().copyThis(
                                        location = Point(0, 3))
                        )),
                ValidPostMoveAgentTestData(world,
                        commandResult.copyThis(
                                playerState = commandResult.getPlayerState().copyThis(
                                        location = Point(0, 3), facing = Direction.EAST)
                ))
        )
    }
}

data class ValidAgentTestData (
        val givenWorld: World,
        val givenCommandResult: CommandResult,
        val expectedCommand: Command
)

data class ValidPostMoveAgentTestData (
        val givenWorld: World,
        val expectedCommandResult: CommandResult
)