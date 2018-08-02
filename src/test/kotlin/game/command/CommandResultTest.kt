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
    private val commandResult = CommandResult(arrayListOf(Perception.BLOCKADE_BUMP, Perception.MOO),
            PlayerState(location = Point(44, 23)), arrayListOf(RoomContent.ARROW))

    @Test
    fun `get perceptions`() {
        assertEquals(arrayListOf(Perception.BLOCKADE_BUMP, Perception.MOO), commandResult.getPerceptions())
    }

    @Test
    fun `get player state`() {
        assertEquals(PlayerState(location = Point(44, 23)).getLocation(), commandResult.getPlayerState().getLocation())
    }

    @Test
    fun `get room content`() {
        assertEquals(arrayListOf(RoomContent.ARROW).toString(), commandResult.getRoomContent().toString())
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