package game.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerState
import game.world.GameObjectFeature
import game.world.Perception

data class CommandResult(private val perceptions: Set<Perception> = setOf(),
                         private val playerState: PlayerState = PlayerState(),
                         private val gameActive: Boolean = true) {
    fun getActive() = gameActive
    fun getPerceptions() = perceptions

    fun getPlayerState() = playerState
    fun isAlive() = getPlayerState().isAlive()
    fun getLocation() = getPlayerState().getLocation()
    fun getDirection() = getPlayerState().getDirection()
    fun getInventory() = getPlayerState().getInventory()
    fun hasItem(item: InventoryItem) = getPlayerState().hasItem(item)
    fun getNumberOf(item: InventoryItem) = getPlayerState().getNumberOf(item)
    fun getScore() = getPlayerState().getScore()
    
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()

    fun copyThis(perceptions: Set<Perception> = this.perceptions,
                 playerState: PlayerState = this.playerState.copyThis(),
                 gameActive: Boolean = this.gameActive) = CommandResult(perceptions, playerState, gameActive)

    override fun toString(): String {
        return "Perceptions: $perceptions\nGame active: $gameActive\n$playerState\n"
    }

    companion object {
        fun createCommandResult(game: Game,
                                perceptions: Set<Perception> = emptySet()): CommandResult {
            val allPerceptions = perceptions.toMutableSet()
            allPerceptions.addAll(createPerceptions(game))
            return CommandResult(allPerceptions.toSet(), game.getPlayerState(), game.getActive())
        }

        internal fun createPerceptions(game: Game): Set<Perception> {
            val perceptionList = mutableSetOf<Perception>()
            val location = game.getPlayerLocation()
            for (content in game.getGameObjects(location).filter { it.hasFeature(GameObjectFeature.Perceptable()) }) {
                (content.getFeature(GameObjectFeature.Perceptable()) as GameObjectFeature.Perceptable)
                        .perception?.let { perceptionList.add(it) }
            }
            return perceptionList
        }
    }
}