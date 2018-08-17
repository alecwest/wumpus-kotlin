package game.agent.intelligence

import game.command.Command
import game.command.CommandResult
import game.world.GameObject
import game.world.World
import game.world.gameObjectValues
import game.world.toGameObjects
import java.awt.Point

class KnowledgeBasedIntelligence2 : Intelligence() {
    internal val facts = FactMap()

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        val perceivedObjects = toGameObjects(commandResult.getPerceptions().toSet()) // TODO change perceptions to a set
        val playerLocation = commandResult.getPlayerState().getLocation()
        for (gameObject in gameObjectValues()) {
            facts.addFact(
                    playerLocation,
                    if (perceivedObjects.contains(gameObject)) Fact.HAS else Fact.HAS_NO,
                    gameObject)
        }
    }
}