package server.command

import game.Perception

class CommandResult(private val perceptions: ArrayList<Perception>) {
    fun getPerceptions() = perceptions
    fun blockadeHit() = getPerceptions().contains(Perception.BUMP)
    fun wallHit() = getPerceptions().contains(Perception.WALL)
}