package game.agent.intelligence

import Helpers.Companion.assertCommandEquals
import game.command.CommandResult
import game.command.MoveCommand
import game.world.RoomContent
import org.junit.jupiter.api.Test
import java.awt.Point

internal class IntelligenceTest {

    @Test
    fun `use basic intelligence`() {
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 4) to arrayListOf(RoomContent.BLOCKADE)))
        val intelligence = BasicIntelligence()
        val commandResult = CommandResult()
        assertCommandEquals(MoveCommand(), intelligence.chooseNextMove(world, commandResult))
    }
}