package game.agent

import game.agent.intelligence.BasicIntelligence
import game.client.Client
import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.command.TurnCommand
import game.player.InventoryItem
import Helpers.Companion.assertContains
import Helpers.Companion.createAgent
import Helpers.Companion.createClient
import Helpers.Companion.createCommandResult
import Helpers.Companion.createPlayerState
import Helpers.Companion.createWorld
import game.world.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import util.toPlayerMapRepresentation
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
        assertContains(agent.client.getMoveResult().getPlayerState().getDirection().toPlayerMapRepresentation(),
                agent.world.getWorldMap(agent.client.getMoveResult().getPlayerState()), 1)
    }

    @ParameterizedTest
    @MethodSource("validBasicIntelligenceAgentTestDataProvider")
    fun `choose next move with basic intelligence`(testData: ValidAgentTestData) {
        assertEquals(testData.expectedCommands, basicIntelligenceAgent.chooseNextMove(testData.givenWorld, testData.givenCommandResult))
    }

    @ParameterizedTest
    @MethodSource("validPostMoveBasicIntelligenceAgentTestDataProvider")
    fun `make next move with basic intelligence`(testData: ValidPostMoveAgentTestData) {
        basicIntelligenceAgent.makeNextMove()
        assertEquals(testData.expectedCommandResult, basicIntelligenceAgent.client.getMoveResult())
    }

    companion object {
    val basicIntelligenceClient = createClient(Helpers.testFilePath + "basicIntelligenceTestFile.json")
        val basicIntelligenceAgent = createAgent(basicIntelligenceClient, BasicIntelligence())
        val world = createWorld(
                gameObject = mapOf(Point(0, 1) to setOf(GameObject.BLOCKADE)))
        val commandResult = createCommandResult(
                playerState = createPlayerState(
                        inventoryContent = mapOf(InventoryItem.ARROW to 1)
                )
        )

        @JvmStatic
        fun validBasicIntelligenceAgentTestDataProvider() = Stream.of(
                ValidAgentTestData(world, commandResult, listOf(MoveCommand())),
                ValidAgentTestData(world, commandResult.copyThis(
                        perceptions = setOf(Perception.BLOCKADE_BUMP),
                        playerState = commandResult.getPlayerState().copyThis(location = Point(0, 0))),
                        listOf(TurnCommand(Direction.EAST)))
        )

        @JvmStatic
        fun validPostMoveBasicIntelligenceAgentTestDataProvider() = Stream.of(
                ValidPostMoveAgentTestData(commandResult.copyThis(
                                playerState = commandResult.getPlayerState().copyThis(location = Point(0, 3))
                        )),
                ValidPostMoveAgentTestData(commandResult.copyThis(
                                perceptions = setOf(Perception.BLOCKADE_BUMP),
                                playerState = commandResult.getPlayerState().copyThis(
                                        location = Point(0, 3))
                        )),
                ValidPostMoveAgentTestData(commandResult.copyThis(
                                playerState = commandResult.getPlayerState().copyThis(
                                        location = Point(0, 3), facing = Direction.EAST)
                        )),
                ValidPostMoveAgentTestData(commandResult.copyThis(
                                setOf(Perception.BREEZE),
                                commandResult.getPlayerState().copyThis(
                                        location = Point(1, 3), facing = Direction.EAST)
                        ))
        )
    }
}

data class ValidAgentTestData (
        val givenWorld: World,
        val givenCommandResult: CommandResult,
        val expectedCommands: List<Command>
)

data class ValidPostMoveAgentTestData (
        val expectedCommandResult: CommandResult
)