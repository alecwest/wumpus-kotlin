package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.GrabCommand
import game.command.MoveCommand
import game.command.TurnCommand
import game.player.InventoryItem
import game.world.GameObject.*
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.*
import java.awt.Point

internal class KnowledgeBasedIntelligence2Test {
    val intelligence = KnowledgeBasedIntelligence2()

    @Test
    fun `process last move in empty room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS_NO, FOOD))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(2, 2), HAS, ARROW))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2).north(), HAS_NO, PIT))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2).southEast(), HAS_NO, SUPMUW))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(2, 2).southEast(), HAS_NO, GLITTER))
    }

    @Test
    fun `process last move in a breeze room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS, BREEZE))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS_NO, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(2, 2).north(), HAS, PIT))
    }

    @Test
    fun `process last move in a corner room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).south(), HAS, PIT))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).west(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).north(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).east(), HAS, PIT))
    }

    @Test
    fun `assess edge room case`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4))))
        assertEquals(true, intelligence.playerOnEdge())
    }

    @Test
    fun `reassess rooms for new insight into danger locations based on most recent perceptions`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0).north())))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(0, 0).north(), HAS_NO, PIT))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(0, 0).east(), HAS, PIT))
    }

    @Test
    fun `use sequence of moves to identify supmuw location`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.MOO),
                Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(1, 0), HAS, SUPMUW))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.MOO),
                Helpers.createPlayerState(location = Point(0, 1))))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(1, 0), HAS, SUPMUW))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(),
                Helpers.createPlayerState(location = Point(0, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(1, 0), HAS, SUPMUW))
    }

    @Test
    fun `choose next move at starting space`() {
        assertEquals(MoveCommand(), intelligence.chooseNextMove(world, Helpers.createCommandResult()))
    }

    @Test
    fun `turn around when danger is met`() {
        assertEquals(TurnCommand(Direction.WEST), intelligence.chooseNextMove(world,
                Helpers.createCommandResult(arrayListOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(2, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `turn to the safe room on right or left when danger is met if it exists`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(),
                Helpers.createPlayerState(location = Point(2, 1))))
        assertEquals(TurnCommand(Direction.SOUTH), intelligence.chooseNextMove(world,
                Helpers.createCommandResult(arrayListOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(2, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `go forward when danger is met if forward facing room is known to be safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                arrayListOf(),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(MoveCommand(), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `grab item in room`() {
        assertEquals(GrabCommand(InventoryItem.GOLD), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                arrayListOf(Perception.GLITTER, Perception.STENCH, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }
}