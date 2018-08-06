package game.command

import game.player.PlayerState
import game.world.Perception
import game.world.RoomContent

data class CommandResult(private val perceptions: ArrayList<Perception> = arrayListOf(),
                         private val playerState: PlayerState = PlayerState(),
                         private val roomContent: ArrayList<RoomContent> = arrayListOf()) {
    fun getPerceptions() = perceptions
    fun getPlayerState() = playerState
    fun getRoomContent() = roomContent
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()

    fun copyThis(perceptions: ArrayList<Perception> = this.perceptions,
                 playerState: PlayerState = this.playerState.copyThis(),
                 roomContent: ArrayList<RoomContent> = this.roomContent) = CommandResult(perceptions, playerState, roomContent)

    override fun toString(): String {
        return "Perceptions: $perceptions\n$playerState\nIn the room: $roomContent\n"
    }


}