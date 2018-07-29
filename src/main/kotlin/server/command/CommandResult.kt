package server.command

import game.world.Perception

data class CommandResult(private val perceptions: ArrayList<Perception> = arrayListOf()) {
    fun getPerceptions() = perceptions
    fun blockadeHit() = getPerceptions().contains(Perception.BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL)
    fun moveRejected() = blockadeHit() || wallHit()
}