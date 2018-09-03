package game.agent.intelligence

import game.command.*
import game.player.InventoryItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.io.ByteArrayInputStream
import java.util.stream.Stream

internal class HumanIntelligenceTest {
    @ParameterizedTest
    @MethodSource("validHumanIntelligenceTestDataProvider")
    fun `choose next move with human intelligence`(testData: ValidHumanIntelligenceTestData) {
        System.setIn(ByteArrayInputStream(testData.givenInput.toByteArray()))
        assertEquals(testData.expectedCommands, HumanIntelligence().chooseNextMoves(IntelligenceTest.world, IntelligenceTest.commandResult))
    }

    companion object {
        @JvmStatic
        fun validHumanIntelligenceTestDataProvider() = Stream.of(
                ValidHumanIntelligenceTestData("m", listOf(MoveCommand())),
                ValidHumanIntelligenceTestData("move\nm", listOf(MoveCommand())),
                ValidHumanIntelligenceTestData("r", listOf(TurnCommand(Direction.EAST))),
                ValidHumanIntelligenceTestData("l", listOf(TurnCommand(Direction.WEST))),
                ValidHumanIntelligenceTestData("s", listOf(ShootCommand(InventoryItem.ARROW))),
                ValidHumanIntelligenceTestData("a", listOf(GrabCommand(InventoryItem.ARROW))),
                ValidHumanIntelligenceTestData("f", listOf(GrabCommand(InventoryItem.FOOD))),
                ValidHumanIntelligenceTestData("g", listOf(GrabCommand(InventoryItem.GOLD)))
        )
    }
}

data class ValidHumanIntelligenceTestData (
    val givenInput: String,
    val expectedCommands: List<Command>
)