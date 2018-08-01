package game.client

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClientTest {
    @Test
    fun `initialize Client session`() {
        assertEquals(0, Client().sessionId)
        assertEquals(1, Client().sessionId)
    }
}