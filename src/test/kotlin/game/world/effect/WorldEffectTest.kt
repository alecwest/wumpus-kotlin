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
    val world = Helpers.createWorld()
    val point = Point(1, 1)

    @ParameterizedTest
    @MethodSource("validWorldEffectTestDataProvider")
    fun `apply affect`(testData: ValidWorldEffectTestData) {
        val world = testData.givenWorld
        val point = testData.givenPoint
        val effect = testData.givenWorldEffect
        assertEquals(testData.expectedReturn, effect.applyEffect(world, point))
        for (roomAffected in effect.roomsAffected(point)) {
            assertEquals(testData.effectObjectInAffectedRooms, world.hasGameObject(roomAffected, effect.gameObject))
        }
    }

    companion object {
        val world = Helpers.createWorld()
        val point = Point(1, 1)

        @JvmStatic
        fun validWorldEffectTestDataProvider() = Stream.of(
                ValidWorldEffectTestData(world, point, AdjacentEffect(GameObject.GOLD),
                        true, true),
                ValidWorldEffectTestData(Helpers.createWorld(1), point, AdjacentEffect(GameObject.GOLD),
                        false, false),
                ValidWorldEffectTestData(world, point, DiagonalEffect(GameObject.GOLD),
                        true, true),
                ValidWorldEffectTestData(Helpers.createWorld(1), point, AdjacentEffect(GameObject.GOLD),
                        false, false),
                ValidWorldEffectTestData(world, point, HereEffect(GameObject.GOLD),
                        true, true),
                ValidWorldEffectTestData(Helpers.createWorld(
                        gameObject = mapOf(Point(2, 2) to setOf(GameObject.PIT))), point,
                        ConditionalEffect(HereEffect(GameObject.GOLD), ProximityCondition(HAS, GameObject.PIT)),
                        true, true),
                ValidWorldEffectTestData(world, point, ConditionalEffect(HereEffect(GameObject.GOLD),
                        ProximityCondition(HAS, GameObject.PIT)),
                        false, false)
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