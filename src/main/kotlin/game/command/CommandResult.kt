package game.command

import game.player.InventoryItem
import game.player.PlayerState
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


}