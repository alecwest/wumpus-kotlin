package game.command

import game.player.PlayerState
import game.world.GameObject
import game.world.Perception

data class CommandResult(private val perceptions: ArrayList<Perception> = arrayListOf(),
                         private val playerState: PlayerState = PlayerState(),
                         private val gameObjects: ArrayList<GameObject> = arrayListOf()) {
    fun getPerceptions() = perceptions
    fun getPlayerState() = playerState
    fun getGameObjects() = gameObjects
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()

    fun copyThis(perceptions: ArrayList<Perception> = this.perceptions,
                 playerState: PlayerState = this.playerState.copyThis(),
                 gameObjects: ArrayList<GameObject> = this.gameObjects) = CommandResult(perceptions, playerState, gameObjects)

    override fun toString(): String {
        return "Perceptions: $perceptions\n$playerState\nIn the room: $gameObjects\n"
    }


}