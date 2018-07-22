package server.command

import game.Game

interface Command {
    var game: Game
        get() = game
        set(value) {
            game =  value
        }
    fun execute()
}