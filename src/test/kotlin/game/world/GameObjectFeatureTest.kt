package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import game.world.effect.*
import game.world.GameObjectFeature.*
import util.northEast

internal class GameObjectFeatureTest {
    val game = Helpers.createGame()

    @Test
    fun `dangerous objects kill player`() {
        assertTrue(Dangerous().killsPlayer(game.getPlayerLocation(), game.getWorld()))
        game.addToRoom(game.getPlayerLocation(), GameObject.PIT)
        assertTrue((game.getGameObjects(game.getPlayerLocation())
                .first { it.hasFeature(Dangerous()) }.getFeature(Dangerous()) as Dangerous)
                .killsPlayer(game.getPlayerLocation(), game.getWorld()))
    }

    @Test
    fun `conditionally dangerous objects kill player on condition`() {
        assertFalse(ConditionallyDangerous(GameObject.GLITTER).killsPlayer(game.getPlayerLocation(), game.getWorld()))
        game.addToRoom(game.getPlayerLocation().northEast(), GameObject.GLITTER)
        assertTrue(ConditionallyDangerous(GameObject.GLITTER).killsPlayer(game.getPlayerLocation(), game.getWorld()))
    }

    @Test
    fun `world affecting feature contains certain world effect class`() {
        val effect = WorldAffecting(arrayListOf(DiagonalEffect(GameObject.GOLD)))
        assertTrue(effect.hasEffectClass(DiagonalEffect::class))
        assertFalse(effect.hasEffectClass(AdjacentEffect::class))
    }
}