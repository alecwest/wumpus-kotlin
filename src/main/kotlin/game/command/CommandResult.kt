package game.command

import game.player.PlayerState
import game.world.Perception
import game.world.RoomContent

data class CommandResult(private val perceptions: ArrayList<Perception> = arrayListOf(),
                         private val playerState: PlayerState = PlayerState(),
                         private val roomContent: ArrayList<RoomContent>) {
    fun getPerceptions() = perceptions
    fun getPlayerState() = playerState
    fun getRoomContent() = roomContent
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()
}