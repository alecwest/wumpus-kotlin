package game.command

import game.world.Perception

class ShootCommand: Command() {
    override fun execute() {
        val perceptionList = arrayListOf<Perception>()

        game.setCommandResult(createCommandResult(perceptionList))
    }
}