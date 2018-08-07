package game.agent.intelligence

import game.command.CommandResult
import game.world.GameObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class IntelligenceTest {
    @Test
    fun `process last move with base method`() {
        val lastMove = Helpers.createCommandResult(arrayListOf(),
                Helpers.createPlayerState(location = Point(4, 4)),
                arrayListOf(GameObject.BREEZE))
        val intelligence = BasicIntelligence()

        intelligence.processLastMove(world, lastMove)
        assertTrue(world.hasGameObject(lastMove.getPlayerState().getLocation(), GameObject.BREEZE))
    }

    companion object {
        val world = Helpers.createWorld(
                gameObject = mapOf(Point(0, 4) to arrayListOf(GameObject.BLOCKADE)))
        val commandResult = CommandResult()
    }
}

