package game.world.effect

import facts.Fact.*
import game.world.GameObject
import game.world.World
import game.world.condition.ProximityCondition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

internal class WorldEffectTest {
    @ParameterizedTest
    @MethodSource("validAddWorldEffectTestDataProvider")
    fun `apply effect`(testData: ValidWorldEffectTestData) {
        val world = testData.givenWorld
        val point = testData.givenPoint
        val effect = testData.givenWorldEffect
        assertEquals(testData.expectedReturn, effect.applyEffect(world, point))
        for (roomAffected in effect.roomsAffected(point)) {
            assertEquals(testData.effectObjectInAffectedRooms, world.hasGameObject(roomAffected, effect.gameObject))
        }
    }

    @ParameterizedTest
    @MethodSource("validRemoveWorldEffectTestDataProvider")
    fun `remove effect`(testData: ValidWorldEffectTestData) {
        val world = testData.givenWorld
        val point = testData.givenPoint
        val effect = testData.givenWorldEffect
        assertEquals(testData.expectedReturn, effect.removeEffect(world, point))
        effect.roomsAffected(point).forEach { roomAffected ->
            assertEquals(testData.effectObjectInAffectedRooms, world.hasGameObject(roomAffected, effect.gameObject))
        }
    }

    companion object {
        @JvmStatic
        fun validAddWorldEffectTestDataProvider() = Stream.of(
                ValidWorldEffectTestData(Helpers.createWorld(), Point(1, 1),
                        AdjacentEffect(GameObject.GOLD),true, true),
                ValidWorldEffectTestData(Helpers.createWorld(1), Point(0, 0),
                        AdjacentEffect(GameObject.GOLD), false, false),
                ValidWorldEffectTestData(Helpers.createWorld(), Point(1, 1),
                        DiagonalEffect(GameObject.GOLD), true, true),
                ValidWorldEffectTestData(Helpers.createWorld(1), Point(0, 0),
                        DiagonalEffect(GameObject.GOLD), false, false),
                ValidWorldEffectTestData(Helpers.createWorld(), Point(1, 1),
                        HereEffect(GameObject.GOLD), true, true),
                ValidWorldEffectTestData(Helpers.createWorld(
                        gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT))), Point(1, 1),
                        ConditionalEffect(HereEffect(GameObject.GOLD), ProximityCondition(HAS, GameObject.PIT)),
                        true, true),
                ValidWorldEffectTestData(Helpers.createWorld(), Point(1, 1),
                        ConditionalEffect(HereEffect(GameObject.GOLD), ProximityCondition(HAS, GameObject.PIT)),
                        false, false)
        )

        @JvmStatic
        fun validRemoveWorldEffectTestDataProvider() = Stream.of(
                ValidWorldEffectTestData(Helpers.createWorld(gameObject = mapOf(Point(1, 2) to setOf(GameObject.GOLD))),
                        Point(1, 1), AdjacentEffect(GameObject.GOLD), true, false),
                ValidWorldEffectTestData(Helpers.createWorld(1), Point(0, 0), AdjacentEffect(GameObject.GOLD),
                        false, false),
                ValidWorldEffectTestData(Helpers.createWorld(gameObject = mapOf(Point(2, 2) to setOf(GameObject.GOLD))),
                        Point(1, 1), DiagonalEffect(GameObject.GOLD), true, false),
                ValidWorldEffectTestData(Helpers.createWorld(1), Point(0, 0),
                        DiagonalEffect(GameObject.GOLD), false, false),
                ValidWorldEffectTestData(Helpers.createWorld(gameObject = mapOf(Point(1, 1) to setOf(GameObject.GOLD))),
                        Point(1, 1), HereEffect(GameObject.GOLD), true, false),
                ValidWorldEffectTestData(Helpers.createWorld(
                        gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT))), Point(1, 1),
                        ConditionalEffect(HereEffect(GameObject.GOLD), ProximityCondition(HAS, GameObject.PIT)),
                        true, false),
                ValidWorldEffectTestData(Helpers.createWorld(), Point(1, 1),
                        ConditionalEffect(HereEffect(GameObject.GOLD), ProximityCondition(HAS, GameObject.PIT)),
                        true, false)
        )
    }
}

data class ValidWorldEffectTestData (
        val givenWorld: World,
        val givenPoint: Point,
        val givenWorldEffect: WorldEffect,
        val expectedReturn: Any?,
        val effectObjectInAffectedRooms: Boolean
)