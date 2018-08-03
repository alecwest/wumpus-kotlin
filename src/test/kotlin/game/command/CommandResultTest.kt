package game.command

import game.player.PlayerState
import game.world.Perception
import game.world.RoomContent
import org.junit.jupiter.api.Test
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CommandResultTest {
    private val commandResult = CommandResult(arrayListOf(Perception.BLOCKADE_BUMP),
            PlayerState(location = Point(44, 23)), arrayListOf(RoomContent.ARROW))
    private val plainCommandResult = CommandResult()

    @Test
    fun `get perceptions`() {
        assertEquals(arrayListOf(Perception.BLOCKADE_BUMP), commandResult.getPerceptions())
        assertEquals(arrayListOf(), plainCommandResult.getPerceptions())
    }

    @Test
    fun `get player state`() {
        assertEquals(PlayerState(location = Point(44, 23)), commandResult.getPlayerState())
        assertEquals(PlayerState(), plainCommandResult.getPlayerState())
    }

    @Test
    fun `get room content`() {
        assertEquals(arrayListOf(RoomContent.ARROW).toString(), commandResult.getRoomContent().toString())
        assertEquals(arrayListOf(), plainCommandResult.getRoomContent())
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