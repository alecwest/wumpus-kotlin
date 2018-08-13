package game.agent.intelligence

import game.command.CommandResult

import game.world.GameObject
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point
internal class IntelligenceTest {
    val intelligence = BasicIntelligence()
    val lastMove = Helpers.createCommandResult(arrayListOf(),
            Helpers.createPlayerState(location = Point(4, 4)))

    @Test
    fun `process last move with base method`() {
        intelligence.processLastMove(world, lastMove)
        assertEquals(arrayListOf(GameObject.BREEZE, GameObject.GLITTER, GameObject.GOLD), world.getGameObjects(lastMove.getPlayerState().getLocation()))
    }

    @Test
    fun `process removal of object(s) from room`() {
        val lastMove2 = lastMove.copyThis(perceptions = arrayListOf(Perception.BREEZE))
        intelligence.processLastMove(world, lastMove)
        intelligence.processLastMove(world, lastMove2)
        assertEquals(arrayListOf(GameObject.BREEZE), world.getGameObjects(lastMove.getPlayerState().getLocation()))
    }

    companion object {
        val world = Helpers.createWorld(
                gameObject = mapOf(Point(0, 4) to arrayListOf(GameObject.BLOCKADE)))
        val commandResult = CommandResult()
    }
}

