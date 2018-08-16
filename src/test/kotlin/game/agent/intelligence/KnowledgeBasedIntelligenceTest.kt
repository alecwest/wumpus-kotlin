package game.agent.intelligence

import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.CommandResult
import game.world.GameObject
import game.world.Perception
import game.world.effect.AdjacentEffect
import game.world.effect.DiagonalEffect
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
        pitDeductionIntelligence.processLastMove(world, testData.lastMove)
        assertEquals(testData.expectedPossibles, pitDeductionIntelligence.possibles.getMap())
        assertEquals(testData.expectedKnowns, pitDeductionIntelligence.knowns.getMap())
    }

    @ParameterizedTest
    @MethodSource("validMoveSupmuwDeductionTestDataProvider")
    fun `process multiple moves to deduce location of supmuw`(testData: ValidMoveProcessingTestData) {
        supmuwDeductionIntelligence.processLastMove(world, testData.lastMove)
        assertEquals(testData.expectedPossibles, supmuwDeductionIntelligence.possibles.getMap())
        assertEquals(testData.expectedKnowns, supmuwDeductionIntelligence.knowns.getMap())
    }

    @ParameterizedTest
    @MethodSource("validPossibleEffectsTestDataProvider")
    fun `get possible effects`(testData: ValidResultAndObjectTestData) {
        assertEquals(testData.expectedResult,
                intelligence.getPossibleEffects(testData.commandResult, testData.gameObject))
    }

    @ParameterizedTest
    @MethodSource("validWasPerceivedTestDataProvider")
    fun `object was perceived`(testData: ValidResultAndObjectTestData) {
        assertEquals(testData.expectedResult,
                intelligence.wasPerceived(testData.commandResult, testData.gameObject))
    }

    @Test
    fun `add known object`() {
        intelligence.addKnownObject(world, Point(0, 3), GameObject.WUMPUS)
        intelligence.addKnownObject(world, Point(12, 13), GameObject.FOOD)
        assertEquals(mutableMapOf(Point(0, 3) to mutableSetOf<GameObject>(GameObject.WUMPUS)), intelligence.knowns.getMap())
    }

    @Test
    fun `add possible object`() {
        intelligence.addPossibleObject(world, Point(0, 3), GameObject.WUMPUS)
        intelligence.addPossibleObject(world, Point(12, 13), GameObject.FOOD)
        assertEquals(mutableMapOf(Point(0, 3) to mutableSetOf<GameObject>(GameObject.WUMPUS)), intelligence.possibles.getMap())
    }

    @Test
    fun `add possible object to room with knowns`() {
        intelligence.addKnownObject(world, Point(0, 3), GameObject.SUPMUW)
        intelligence.addPossibleObject(world, Point(0, 3), GameObject.WUMPUS)
        assertEquals(mutableMapOf(Point(0, 3) to mutableSetOf<GameObject>(GameObject.WUMPUS)), intelligence.possibles.getMap())
    }

    companion object {
        val lastMove = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)))

        val pitDeductionIntelligence = KnowledgeBasedIntelligence()
        val supmuwDeductionIntelligence = KnowledgeBasedIntelligence()

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
        fun validMoveSupmuwDeductionTestDataProvider() = Stream.of(
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(Perception.MOO),
                        Helpers.createPlayerState(location = Point(4, 3))),
                        mutableMapOf(Point(4, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(3, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(5, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(3, 3) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(5, 3) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(3, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(4, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(5, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL)),
                        mutableMapOf(Point(4, 3) to mutableSetOf<GameObject>(GameObject.MOO))),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(Perception.MOO),
                        Helpers.createPlayerState(location = Point(3, 3))),
                        mutableMapOf(Point(4, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(3, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(3, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(4, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL),
                                Point(5, 2) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL)),
                        mutableMapOf(Point(4, 3) to mutableSetOf<GameObject>(GameObject.MOO),
                                Point(3, 3) to mutableSetOf<GameObject>(GameObject.MOO))),
                ValidMoveProcessingTestData(lastMove.copyThis(arrayListOf(Perception.MOO),
                        Helpers.createPlayerState(location = Point(3, 4))),
                        mutableMapOf(),
                        mutableMapOf(Point(4, 3) to mutableSetOf<GameObject>(GameObject.MOO),
                                Point(3, 3) to mutableSetOf<GameObject>(GameObject.MOO),
                                Point(3, 4) to mutableSetOf<GameObject>(GameObject.MOO),
                                Point(4, 4) to mutableSetOf(GameObject.SUPMUW, GameObject.SUPMUW_EVIL)))
        )

        @JvmStatic
        fun validPossibleEffectsTestDataProvider() = Stream.of(
                ValidResultAndObjectTestData(lastMove, GameObject.PIT, listOf(AdjacentEffect(GameObject.BREEZE))),
                ValidResultAndObjectTestData(lastMove, GameObject.SUPMUW, listOf<WorldEffect>()),
                ValidResultAndObjectTestData(lastMove.copyThis(arrayListOf(Perception.MOO, Perception.STENCH)),
                        GameObject.SUPMUW,
                        listOf(AdjacentEffect(GameObject.MOO), DiagonalEffect(GameObject.MOO))),
                ValidResultAndObjectTestData(lastMove.copyThis(arrayListOf(Perception.MOO, Perception.STENCH)),
                        GameObject.WUMPUS,
                        listOf<WorldEffect>(AdjacentEffect(GameObject.STENCH)))
        )

        @JvmStatic
        fun validWasPerceivedTestDataProvider() = Stream.of(
                ValidResultAndObjectTestData(lastMove, GameObject.STENCH, false),
                ValidResultAndObjectTestData(lastMove, GameObject.BREEZE, true),
                ValidResultAndObjectTestData(lastMove.copyThis(arrayListOf(Perception.GLITTER, Perception.FOOD)),
                        GameObject.GLITTER, true)

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