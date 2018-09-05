package game.agent.intelligence

import facts.FactMap
import facts.Fact
import facts.Answer
import game.world.GameObject
import game.world.Perception
import game.world.gameObjectValues
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Point

internal class FactProcessorTest {
    val world = Helpers.createWorld()
    val facts = FactMap()

    @Test
    fun `assess edge room case`() {
        Assertions.assertEquals(true, FactProcessor.playerOnEdge(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4)))))
        Assertions.assertEquals(false, FactProcessor.playerOnEdge(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(1, 4)))))
    }

    @Test
    fun `assess the current room`() {
        val commandResult = Helpers.createCommandResult(setOf(Perception.BREEZE, Perception.WALL_BUMP))
        FactProcessor.assessCurrentRoom(facts, world, commandResult)
        Assertions.assertTrue(facts.isTrue(Point(0, 0), Fact.HAS, GameObject.BREEZE) == Answer.TRUE)
        Assertions.assertTrue(facts.isTrue(Point(0, 0), Fact.HAS, GameObject.WALL) == Answer.FALSE)
        Assertions.assertTrue(facts.isTrue(Point(0, 0), Fact.HAS_NO, GameObject.STENCH) == Answer.TRUE)
    }

    @Test
    fun `add blocking object`() {
        val commandResult = Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(4, 4)))
        FactProcessor.addBlockingObject(facts, world, commandResult, GameObject.WALL)
        Assertions.assertEquals(Answer.TRUE, facts.isTrue(Point(4, 4), Fact.HAS_NO, GameObject.WALL))
        Assertions.assertEquals(Answer.TRUE, facts.isTrue(Point(4, 5), Fact.HAS, GameObject.WALL))
        FactProcessor.addBlockingObject(facts, world, commandResult, GameObject.STENCH)
        Assertions.assertEquals(Answer.UNKNOWN, facts.isTrue(Point(4, 5), Fact.HAS, GameObject.STENCH))
    }

    @Test
    fun `assess nearby rooms`() {
        val commandResult = Helpers.createCommandResult(setOf(Perception.BREEZE),
                playerState = Helpers.createPlayerState(location = Point(4, 4)))
        facts.addFact(Point(4, 4), Fact.HAS_NO, GameObject.BREEZE)
        facts.addFact(Point(4, 4), Fact.HAS, GameObject.STENCH)
        FactProcessor.assessNearbyRooms(facts, Point(4, 4))
        Assertions.assertEquals(Answer.TRUE, facts.isTrue(Point(4, 5), Fact.HAS_NO, GameObject.PIT))
        Assertions.assertEquals(Answer.UNKNOWN, facts.isTrue(Point(4, 5), Fact.HAS_NO, GameObject.WUMPUS))
    }

    @Test
    fun `reassess for new insight`() {
        facts.addFact(Point(4, 4), Fact.HAS, GameObject.BREEZE)
        facts.addFact(Point(4, 5), Fact.HAS_NO, GameObject.PIT)
        facts.addFact(Point(4, 3), Fact.HAS_NO, GameObject.PIT)
        FactProcessor.reassessForNewInsight(facts)
        Assertions.assertEquals(Answer.UNKNOWN, facts.isTrue(Point(5, 4), Fact.HAS, GameObject.PIT))
        facts.addFact(Point(3, 4), Fact.HAS_NO, GameObject.PIT)
        FactProcessor.reassessForNewInsight(facts)
        Assertions.assertEquals(Answer.TRUE, facts.isTrue(Point(5, 4), Fact.HAS, GameObject.PIT))
    }

    @Test
    fun `mark edge rooms`() {
        FactProcessor.markEdgeRooms(facts, world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(4, 4))))
        Assertions.assertEquals(0, facts.getMap().size)
        FactProcessor.markEdgeRooms(facts, world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(0, 4))))
        Assertions.assertEquals(1, facts.getMap().size)
        for (gameObject in gameObjectValues()) {
            Assertions.assertEquals(Answer.TRUE, facts.isTrue(Point(-1, 4), Fact.HAS_NO, gameObject))
        }
    }


}