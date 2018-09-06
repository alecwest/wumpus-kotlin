package game.world.effect

import game.world.GameObject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.adjacents
import util.diagonals
import game.world.World
import java.awt.Point

class NoEffectTest {
    private val world: World = World(3)
    private val pointToAddTo: Point = Point(1, 1)

    @Test
    fun `add nothing to rooms as effect of blockade`() {
        world.addGameObjectAndEffects(pointToAddTo, GameObject.BLOCKADE)
        for(point in pointToAddTo.adjacents() + pointToAddTo.diagonals()) {
            assertTrue(world.roomIsEmpty(point))
        }
    }
}