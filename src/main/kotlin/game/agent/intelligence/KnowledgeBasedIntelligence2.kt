package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.Command
import game.command.CommandResult
import game.world.*
import game.world.GameObjectFeature.*

class KnowledgeBasedIntelligence2 : Intelligence() {
    internal val facts = FactMap()

    override fun chooseNextMove(world: World, commandResult: CommandResult): Command {
        return super.chooseNextMove(world, commandResult)
    }

    override fun processLastMove(world: World, commandResult: CommandResult) {
        super.processLastMove(world, commandResult)
        val perceivedObjects = toGameObjects(commandResult.getPerceptions().toSet()) // TODO change perceptions to a set
        val playerLocation = commandResult.getPlayerState().getLocation()
        gameObjectValues().forEach { gameObject ->
            facts.addFact(
                    playerLocation,
                    if (perceivedObjects.contains(gameObject)) HAS else HAS_NO,
                    gameObject)
        }
        gameObjectsWithFeatures(setOf(WorldAffecting())).forEach { gameObject ->
            val worldAffecting = gameObject.getFeature(WorldAffecting()) as WorldAffecting
            worldAffecting.effects.forEach { worldEffect ->
                if (facts.isTrue(playerLocation, HAS_NO, worldEffect.gameObject) == TRUE) {
                    worldEffect.roomsAffected(playerLocation).forEach { roomAffected ->
                        facts.addFact(roomAffected, HAS_NO, gameObject)
                    }
                }
            }
        }
    }
}