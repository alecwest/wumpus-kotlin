package server.command

import game.world.Perception

data class CommandResult(private val perceptions: ArrayList<Perception>) {
    fun getPerceptions() = perceptions
    fun blockadeHit() = getPerceptions().contains(Perception.BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL)
    fun moveRejected() = blockadeHit() || wallHit()
}