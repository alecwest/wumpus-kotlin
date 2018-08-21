package game.command

import game.player.PlayerState
import game.world.GameObject
import game.world.Perception

data class CommandResult(private val perceptions: Set<Perception> = setOf(),
                         private val playerState: PlayerState = PlayerState()) {
    fun getPerceptions() = perceptions
    fun getPlayerState() = playerState
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()

    fun copyThis(perceptions: Set<Perception> = this.perceptions,
                 playerState: PlayerState = this.playerState.copyThis()) = CommandResult(perceptions, playerState)

    override fun toString(): String {
        return "Perceptions: $perceptions\n$playerState\n"
    }


}