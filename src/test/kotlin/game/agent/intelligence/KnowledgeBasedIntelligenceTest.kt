package game.agent.intelligence

import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.CommandResult
import game.world.GameObject
import game.world.Perception
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
        assertEquals(testData.expectedPossibles, intelligence.possibles)
        assertEquals(testData.expectedKnowns, intelligence.knowns)
    }

    companion object {
        val lastMove = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)))

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
    }

    @Test
    fun `process move without marking visited rooms as possible for content`() {
        val lastMove = Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(0, 0)))
        val lastMove2 = Helpers.createCommandResult(arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 1)))
        intelligence.processLastMove(world, lastMove)
        intelligence.processLastMove(world, lastMove2)
        assertEquals(mapOf(Point(0, 2) to setOf(GameObject.PIT),
                Point(1, 1) to setOf(GameObject.PIT)), intelligence.possibles)
        assertEquals(mapOf<Point, Set<GameObject>>(Point(0, 0) to setOf(),
                Point(0, 1) to setOf(GameObject.BREEZE)), intelligence.knowns)
    }
}

data class ValidMoveProcessingTestData (
        val lastMove: CommandResult,
        val expectedPossibles: MutableMap<Point, MutableSet<GameObject>>,
        val expectedKnowns: MutableMap<Point, MutableSet<GameObject>>
)