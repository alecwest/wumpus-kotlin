package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.GameObject
import game.world.World
import java.awt.Point

class KnowledgeBasedIntelligence: Intelligence() {
    internal val knowns: Map<Point, Set<GameObject>> = mapOf()
    internal val possibles: Map<Point, Set<GameObject>> = mapOf()

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
    }

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }
}