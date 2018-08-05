package game.agent.intelligence

import game.command.*
import game.player.InventoryItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
        assertEquals(testData.expectedCommand, HumanIntelligence().chooseNextMove(IntelligenceTest.world, IntelligenceTest.commandResult))
    }

    companion object {
        @JvmStatic
        fun validHumanIntelligenceTestDataProvider() = Stream.of(
                ValidHumanIntelligenceTestData("m", MoveCommand()),
                ValidHumanIntelligenceTestData("move\nm", MoveCommand()),
                ValidHumanIntelligenceTestData("r", TurnCommand(Direction.EAST)),
                ValidHumanIntelligenceTestData("l", TurnCommand(Direction.WEST)),
                ValidHumanIntelligenceTestData("s", ShootCommand()),
                ValidHumanIntelligenceTestData("a", GrabCommand(InventoryItem.ARROW)),
                ValidHumanIntelligenceTestData("f", GrabCommand(InventoryItem.FOOD)),
                ValidHumanIntelligenceTestData("g", GrabCommand(InventoryItem.GOLD))
        )
    }
}

data class ValidHumanIntelligenceTestData (
    val givenInput: String,
    val expectedCommand: Command
)