package game.command

import game.player.PlayerState
import game.world.Perception
import game.world.GameObject
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CommandResultTest {
    private val commandResult = CommandResult(setOf(Perception.BLOCKADE_BUMP),
            PlayerState(location = Point(44, 23)))
    private val plainCommandResult = CommandResult()

    @Test
    fun `get perceptions`() {
        assertEquals(setOf(Perception.BLOCKADE_BUMP), commandResult.getPerceptions())
        assertEquals(setOf(), plainCommandResult.getPerceptions())
    }

    @Test
    fun `get player state`() {
        assertEquals(PlayerState(location = Point(44, 23)), commandResult.getPlayerState())
        assertEquals(PlayerState(), plainCommandResult.getPlayerState())
    }

    @Test
    fun `check blockade hit`() {
        assertTrue(commandResult.blockadeHit())
    }

    @Test
    fun `check wall hit`() {
        assertFalse(commandResult.wallHit())
    }

    @Test
    fun `check move rejected`() {
        assertTrue(commandResult.moveRejected())
    }
}