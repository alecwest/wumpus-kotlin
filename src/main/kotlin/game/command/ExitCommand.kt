package game.command

import game.command.CommandResult.Companion.createCommandResult
import game.world.gameObjectsWithFeatures
import game.world.GameObjectFeature.Exitable

/**
 * ExitCommand ends the game if the player is an [Exitable] room
 */
class ExitCommand: Command() {
    override fun execute() {
        game?.let { game ->
            game.setPlayerScore(game.getScore() + getMoveCost(game.getPlayerState()))
            if (gameObjectsWithFeatures(setOf(Exitable())).any {
                        game.hasGameObject(game.getPlayerLocation(), it) }) {
                game.setActive(false)
            }
            game.setCommandResult(createCommandResult(game))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExitCommand) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "ExitCommand()"
    }
}