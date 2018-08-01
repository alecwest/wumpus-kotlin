package game.command

import game.world.Perception

data class CommandResult(private val perceptions: ArrayList<Perception> = arrayListOf()) {
    fun getPerceptions() = perceptions
    fun blockadeHit() = getPerceptions().contains(Perception.BLOCKADE_BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL_BUMP)
    fun moveRejected() = blockadeHit() || wallHit()
}