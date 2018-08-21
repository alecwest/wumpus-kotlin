package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.agent.intelligence.IntelligenceTest.Companion.world
import game.command.GrabCommand
import game.command.MoveCommand
import game.command.TurnCommand
import game.player.InventoryItem
import game.world.GameObject
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
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS, BREEZE))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(2, 2), HAS_NO, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(2, 2).north(), HAS, PIT))
    }

    @Test
    fun `process last move in a corner room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).south(), HAS, PIT))
        assertEquals(FALSE, intelligence.facts.isTrue(Point(0, 0).west(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).north(), HAS, PIT))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(0, 0).east(), HAS, PIT))
    }

    @Test
    fun `assess edge room case`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4))))
        assertEquals(true, intelligence.playerOnEdge())
    }

    @Test
    fun `reassess rooms for new insight into danger locations based on most recent perceptions`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 0).north())))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(0, 0).north(), HAS_NO, PIT))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(0, 0).east(), HAS, PIT))
    }

    @Test
    fun `use sequence of moves to identify supmuw location`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.MOO),
                Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(1, 0), HAS, SUPMUW))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.MOO),
                Helpers.createPlayerState(location = Point(0, 1))))
        assertEquals(UNKNOWN, intelligence.facts.isTrue(Point(1, 0), HAS, SUPMUW))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
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
                Helpers.createCommandResult(setOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(2, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `turn to the safe room on right or left when danger is met if it exists`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(2, 1))))
        assertEquals(TurnCommand(Direction.SOUTH), intelligence.chooseNextMove(world,
                Helpers.createCommandResult(setOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(2, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `go forward when danger is met if forward facing room is known to be safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(MoveCommand(), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `grab item in room`() {
        assertEquals(GrabCommand(InventoryItem.GOLD), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.STENCH, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `grab multiple items in room`() {
        assertEquals(GrabCommand(InventoryItem.FOOD), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.STENCH, Perception.FOOD, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
        assertEquals(GrabCommand(InventoryItem.GOLD), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.STENCH, Perception.GLITTER, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `check object or HereEffect in room`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.BREEZE, Perception.FOOD),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertTrue(intelligence.objectOrHereEffectInRoom(GameObject.GOLD))
        assertTrue(intelligence.objectOrHereEffectInRoom(GameObject.GLITTER))
        assertTrue(intelligence.objectOrHereEffectInRoom(GameObject.FOOD))
        assertFalse(intelligence.objectOrHereEffectInRoom(GameObject.PIT))
    }

    @Test
    fun `forward facing room is safe`() {
        val commandResult1 = Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.FOOD),
                Helpers.createPlayerState(location = Point(2, 2)))
        val commandResult2 = Helpers.createCommandResult(
                setOf(Perception.SCREAM, Perception.BREEZE, Perception.FOOD),
                Helpers.createPlayerState(location = Point(5, 5)))
        val commandResult3 = Helpers.createCommandResult(
                setOf(Perception.SCREAM, Perception.BREEZE, Perception.FOOD),
                Helpers.createPlayerState(location = Point(5, 6),
                        facing = Direction.SOUTH))
        intelligence.processLastMove(world, commandResult1)
        assertTrue(intelligence.forwardFacingRoomIsSafe(commandResult1))
        intelligence.processLastMove(world, commandResult2)
        assertFalse(intelligence.forwardFacingRoomIsSafe(commandResult2))
        intelligence.processLastMove(world, commandResult3)
        assertTrue(intelligence.forwardFacingRoomIsSafe(commandResult3))
    }

    @Test
    fun `dangerous effects in room`() {
        val commandResult1 = Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.FOOD),
                Helpers.createPlayerState(location = Point(2, 2)))
        val commandResult2 = Helpers.createCommandResult(
                setOf(Perception.SCREAM, Perception.BREEZE, Perception.FOOD),
                Helpers.createPlayerState(location = Point(5, 5)))
        intelligence.processLastMove(world, commandResult1)
        assertFalse(intelligence.dangerousEffectsInRoom(commandResult1))
        intelligence.processLastMove(world, commandResult2)
        assertTrue(intelligence.dangerousEffectsInRoom(commandResult2))
    }

    @Test
    fun `turn to safe room`() {
        val commandResult1 = Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 2),
                        facing = Direction.NORTH))
        val commandResult2 = Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(1, 2),
                        facing = Direction.NORTH))
        intelligence.processLastMove(world, commandResult1)
        assertEquals(TurnCommand(Direction.SOUTH),
                intelligence.turnToSafeRoom(world, commandResult1))
        intelligence.processLastMove(world, commandResult2)
        assertEquals(TurnCommand(Direction.EAST),
                intelligence.turnToSafeRoom(world, commandResult2))
    }

    @Test
    fun `turn to safe room when not necessary`() {
        val commandResult = Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(5, 5),
                        facing = Direction.NORTH))
        intelligence.processLastMove(world, commandResult)
        assertEquals(TurnCommand(Direction.EAST),
                intelligence.turnToSafeRoom(world, commandResult))
    }

    @Test
    fun `perceiving a blocking object should add a fact about it to the forward facing room`() {
        val commandResult = Helpers.createCommandResult(
                setOf(Perception.WALL_BUMP),
                Helpers.createPlayerState(location = Point(5, 5),
                        facing = Direction.NORTH))
        intelligence.processLastMove(world, commandResult)
        assertEquals(TRUE, intelligence.facts.isTrue(Point(5, 5), HAS_NO, GameObject.WALL))
        assertEquals(TRUE, intelligence.facts.isTrue(Point(5, 6), HAS, GameObject.WALL))
    }

    @Test
    fun `turn away from blocking rooms`() {
        val commandResult = Helpers.createCommandResult(
                setOf(Perception.WALL_BUMP),
                Helpers.createPlayerState(location = Point(5, 5),
                        facing = Direction.NORTH))
        intelligence.processLastMove(world, commandResult)
        assertEquals(TurnCommand(Direction.EAST), intelligence.chooseNextMove(world, commandResult))
    }
}