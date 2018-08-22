package game.agent.intelligence

import game.command.CommandResult

import game.world.GameObject
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point
internal class IntelligenceTest {
    val intelligence = BasicIntelligence()
    val lastMove = Helpers.createCommandResult(setOf(Perception.BREEZE, Perception.GLITTER),
            Helpers.createPlayerState(location = Point(4, 4)))

    @Test
    fun `process last move with base method`() {
        intelligence.processLastMove(world, lastMove)
        assertEquals(setOf(GameObject.BREEZE, GameObject.GLITTER), world.getGameObjects(lastMove.getPlayerState().getLocation()))
    }

    @Test
    fun `process removal of object(s) from room`() {
        val lastMove2 = lastMove.copyThis(perceptions = setOf(Perception.BREEZE))
        intelligence.processLastMove(world, lastMove)
        intelligence.processLastMove(world, lastMove2)
        assertEquals(setOf(GameObject.BREEZE), world.getGameObjects(lastMove.getPlayerState().getLocation()))
    }

    @Test
    fun `reset room without removing content from other adjacent rooms`() {
        world.addGameObject(Point(2, 1), GameObject.GLITTER)
        world.addGameObject(Point(2, 2), GameObject.STENCH)

        intelligence.resetRoom(world, Helpers.createCommandResult(setOf(Perception.GLITTER),
                Helpers.createPlayerState(location = Point(2, 1))))
        assertFalse(world.hasGameObject(Point(2, 1), GameObject.GLITTER))
        assertTrue(world.hasGameObject(Point(2, 2), GameObject.STENCH))
    }

    companion object {
        val world = Helpers.createWorld(
                gameObject = mapOf(Point(0, 4) to setOf(GameObject.BLOCKADE)))
        val commandResult = CommandResult()
    }
}

