package game.command

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

internal class ExitCommandTest {
    @Test
    fun `exit commands are equal`() {
        val command = ExitCommand()
        assertEquals(command, command)
        assertNotEquals(command, 123)
        assertEquals(command, ExitCommand())
    }

    @Test
    fun `execute exit command in exitable room`() {
        val game = Helpers.createGame(player = Helpers.createPlayer(location = Point(0, 0)))
        val command = ExitCommand()
        command.setGame(game)
        command.execute()
        assertEquals(Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(0, 0), score = 1)), game.getCommandResult())
    }

    @Test
    fun `execute exit command in un-exitable room`() {
        val game = Helpers.createGame(player = Helpers.createPlayer(location = Point(1, 1)))
        val command = ExitCommand()
        command.setGame(game)
        command.execute()
        assertEquals(Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(1, 1), score = 1)), game.getCommandResult())
    }
}