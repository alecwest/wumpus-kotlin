package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.command.MoveCommand
import game.command.TurnCommand
import game.world.Perception
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.io.ByteArrayInputStream
import java.util.stream.Stream

internal class IntelligenceTest {
    @Test
    fun `process last move with base method`() {
        val lastMove = Helpers.createCommandResult(arrayListOf(),
                Helpers.createPlayerState(location = Point(4, 4)),
                arrayListOf(RoomContent.BREEZE))
        val intelligence = BasicIntelligence()

        intelligence.processLastMove(world, lastMove)
        assertTrue(world.hasRoomContent(lastMove.getPlayerState().getLocation(), RoomContent.BREEZE))
    }

    companion object {
        val world = Helpers.createWorld(
                roomContent = mapOf(Point(0, 4) to arrayListOf(RoomContent.BLOCKADE)))
        val commandResult = CommandResult()
    }
}

