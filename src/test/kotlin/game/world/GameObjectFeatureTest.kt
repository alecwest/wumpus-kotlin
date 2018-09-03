package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import game.world.GameObjectFeature.*
import util.northEast

internal class GameObjectFeatureTest {
    val game = Helpers.createGame()

    @Test
    fun `dangerous objects kill player`() {
        assertTrue(Dangerous().killsPlayer(game))
        game.addToRoom(game.getPlayerLocation(), GameObject.PIT)
        assertTrue((game.getGameObjects(game.getPlayerLocation())
                .first { it.hasFeature(Dangerous()) }.getFeature(Dangerous()) as Dangerous)
                .killsPlayer(game))
    }

    @Test
    fun `conditionally dangerous objects kill player on condition`() {
        assertFalse(ConditionallyDangerous(GameObject.GLITTER).killsPlayer(game))
        game.addToRoom(game.getPlayerLocation().northEast(), GameObject.GLITTER)
        assertTrue(ConditionallyDangerous(GameObject.GLITTER).killsPlayer(game))
    }
}