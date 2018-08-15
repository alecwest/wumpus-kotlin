package game.agent.intelligence

import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.CommandResult
import game.world.GameObject
import game.world.Perception
import game.world.effect.AdjacentEffect
import game.world.effect.WorldEffect
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

internal class KnowledgeBasedIntelligenceTest {
    val intelligence = KnowledgeBasedIntelligence()

    @ParameterizedTest
    @MethodSource("validMoveProcessingTestDataProvider")
    fun `process last move with knowns and possibles`(testData: ValidMoveProcessingTestData) {
        intelligence.processLastMove(world, testData.lastMove)
        assertEquals(testData.expectedPossibles, intelligence.possibles.getMap())
        assertEquals(testData.expectedKnowns, intelligence.knowns.getMap())
    }

    @ParameterizedTest
    @MethodSource("validMovePitDeductionTestDataProvider")
    fun `process multiple moves to deduce location of pit`(testData: ValidMoveProcessingTestData) {
        companionIntelligence.processLastMove(world, testData.lastMove)
        assertEquals(testData.expectedPossibles, companionIntelligence.possibles.getMap())
        assertEquals(testData.expectedKnowns, companionIntelligence.knowns.getMap())
    }

    @ParameterizedTest
    @MethodSource("validPossibleEffectsTestDataProvider")
    fun `get possible effects`(testData: ValidResultAndObjectTestData) {
        assertEquals(testData.expectedResult,
                intelligence.getPossibleEffects(testData.commandResult, testData.gameObject))
    }

    companion object {
        val lastMove = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)))

        val companionIntelligence = KnowledgeBasedIntelligence()

        @JvmStatic
        fun validMoveProcessingTestDataProvider() = Stream.of(
                ValidMoveProcessingTestData(lastMove,
                        mutableMapOf(Point(0, 0) to mutableSetOf<GameObject>(GameObject.PIT),
                                Point(0, 2) to mutableSetOf<GameObject>(GameObject.PIT),
                                Point(1, 1) to mutableSetOf<GameObject>(GameObject.PIT)),
                        mutableMapOf(Point(0, 1) to mutableSetOf<GameObject>(GameObject.BREEZE))),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(Perception.MOO)),
                        mutableMapOf(Point(0, 0) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(0, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(1, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(1, 0) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(1, 1) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL)),
                        mutableMapOf(Point(0, 1) to mutableSetOf<GameObject>(GameObject.MOO))),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(Perception.GLITTER)),
                        mutableMapOf(),
                        mutableMapOf(Point(0, 1) to mutableSetOf(GameObject.GLITTER, GameObject.GOLD)))
        )

        @JvmStatic
        fun validMovePitDeductionTestDataProvider() = Stream.of(
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(), Helpers.createPlayerState(location = Point(0, 0))),
                        mutableMapOf(),
                        mutableMapOf(Point(0, 0) to mutableSetOf())),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(), Helpers.createPlayerState(location = Point(0, 1))),
                        mutableMapOf(),
                        mutableMapOf(Point(0, 0) to mutableSetOf(), Point(0, 1) to mutableSetOf())),
                // Player should be able to deduce that both (0, 1) and (1, 0) are not possible pit locations
                // since (0, 1) has been visited and there was no breeze in (0, 0)
                ValidMoveProcessingTestData(lastMove.copyThis(playerState = Helpers.createPlayerState(location = Point(1, 1))),
                        mutableMapOf(Point(1, 2) to mutableSetOf<GameObject>(GameObject.PIT),
                                Point(2, 1) to mutableSetOf<GameObject>(GameObject.PIT)),
                        mutableMapOf(Point(0, 0) to mutableSetOf(),
                                Point(0, 1) to mutableSetOf(),
                                Point(1, 1) to mutableSetOf<GameObject>(GameObject.BREEZE))),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(), Helpers.createPlayerState(location = Point(1, 0))),
                        mutableMapOf(Point(1, 2) to mutableSetOf<GameObject>(GameObject.PIT),
                                Point(2, 1) to mutableSetOf<GameObject>(GameObject.PIT)),
                        mutableMapOf(Point(0, 0) to mutableSetOf(),
                                Point(0, 1) to mutableSetOf(),
                                Point(1, 1) to mutableSetOf<GameObject>(GameObject.BREEZE),
                                Point(1, 0) to mutableSetOf())),
                // Player should be able to deduce the location of a pit upon finding (2, 0) to be empty
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(), Helpers.createPlayerState(location = Point(2, 0))),
                        mutableMapOf(),
                        mutableMapOf(Point(0, 0) to mutableSetOf(),
                                Point(0, 1) to mutableSetOf(),
                                Point(1, 1) to mutableSetOf<GameObject>(GameObject.BREEZE),
                                Point(1, 0) to mutableSetOf(),
                                Point(2, 0) to mutableSetOf(),
                                Point(1, 2) to mutableSetOf<GameObject>(GameObject.PIT)))
        )

        @JvmStatic
        fun validPossibleEffectsTestDataProvider() = Stream.of(
                ValidResultAndObjectTestData(lastMove, GameObject.PIT, listOf(AdjacentEffect(GameObject.BREEZE))),
                ValidResultAndObjectTestData(lastMove, GameObject.SUPMUW, listOf<WorldEffect>())
        )
    }
}

data class ValidMoveProcessingTestData (
        val lastMove: CommandResult,
        val expectedPossibles: MutableMap<Point, MutableSet<GameObject>>,
        val expectedKnowns: MutableMap<Point, MutableSet<GameObject>>
)

data class ValidResultAndObjectTestData (
        val commandResult: CommandResult,
        val gameObject: GameObject,
        val expectedResult: Any
)