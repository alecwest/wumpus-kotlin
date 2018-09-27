package game.command

import game.command.CommandResult.Companion.createCommandResult
import game.player.PlayerState
import game.world.Perception
import game.world.GameObjectFeature.*
import util.*
import java.awt.Point

/**
 * MoveCommand moves the player in the direction they are facing
 */
class MoveCommand: Command() {
    override fun execute() {
        game?.let { game ->
            val direction = game.getPlayerDirection()
            val targetLocation = game.getPlayerLocation().adjacent(direction)
            val perceptionList = mutableSetOf<Perception>()

            when {
                canEnterRoom(targetLocation) -> {
                    game.setPlayerLocation(
                            game.getPlayerLocation().adjacent(game.getPlayerDirection()))
                }
                game.roomIsValid(targetLocation) -> {
                    for (gameObject in game.getGameObjects(targetLocation).filter { it.hasFeature(Blocking()) }) {
                        val perception = (gameObject.getFeature(Perceptable()) as Perceptable).perception
                        if (perception != null) perceptionList.add(perception)
                    }
                }
                else -> perceptionList.add(Perception.WALL_BUMP)
            }

            game.setPlayerScore(game.getScore() + getMoveCost(game.getPlayerState()))
            game.setCommandResult(createCommandResult(game, perceptionList))
        }
    }

    override fun getMoveCost(playerState: PlayerState?): Int {
        game?.let { game ->
            if (!game.isPlayerAlive())
                // TODO death cost should probably be somewhere else
                return super.getMoveCost(playerState) + 1000
        }
        return super.getMoveCost(playerState)
    }

    private fun canEnterRoom(point: Point): Boolean {
        game?.let { game ->
            if(game.getGameObjects(point).any { it.hasFeature(Blocking()) } || !game.roomIsValid(point)) {
                return false
            }
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MoveCommand) return false
        return true
    }

    override fun toString(): String {
        return "MoveCommand()"
    }
}
