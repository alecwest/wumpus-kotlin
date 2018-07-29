package server.command

import game.world.Perception
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CommandResultTest {
    private val commandResult = CommandResult(arrayListOf(Perception.BUMP, Perception.MOO))

    @Test
    fun `get perceptions`() {
        assertEquals(arrayListOf(Perception.BUMP, Perception.MOO), commandResult.getPerceptions())
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