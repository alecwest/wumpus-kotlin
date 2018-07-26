package server.command

import game.Game

interface Command {
    // TODO this should be in the constructor for each Command?
    var game: Game
        get() = game
        set(value) {
            game =  value
        }
    fun execute()
}