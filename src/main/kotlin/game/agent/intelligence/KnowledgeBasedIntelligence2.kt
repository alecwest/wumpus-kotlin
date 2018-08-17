package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.GameObject
import game.world.World
import java.awt.Point

class KnowledgeBasedIntelligence2: Intelligence() {
    internal val facts: Map<Point, Set<Pair<GameObject, Boolean>>> = mapOf()

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
    }
}