package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
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

internal class KnowledgeBasedIntelligenceTest {
    val intelligence = KnowledgeBasedIntelligence()
    val world = Helpers.createWorld()

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
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(), Helpers.createPlayerState(location = Point(7, 2),
                facing = Direction.EAST)))
        assertEquals(TurnCommand(Direction.WEST), intelligence.chooseNextMove(world,
                Helpers.createCommandResult(setOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(8, 2),
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
    fun `do not turn around when danger is met if other nearby room is known to be safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(3, 8))))
        assertNotEquals(TurnCommand(Direction.WEST), intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(3, 9), facing = Direction.EAST))))
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
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(4, 5))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(5, 4))))
        assertEquals(TurnCommand(Direction.EAST),
                intelligence.chooseNextMove(world, Helpers.createCommandResult(
                setOf(Perception.WALL_BUMP),
                Helpers.createPlayerState(location = Point(5, 5),
                        facing = Direction.NORTH))))
    }

    @Test
    fun `choose to make the shortest turn to an unknown room when safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(),
                Helpers.createPlayerState(location = Point(4, 5))))
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(),
                Helpers.createPlayerState(location = Point(5, 4))))
        assertNotEquals(TurnCommand(Direction.SOUTH),
                intelligence.chooseNextMove(world, Helpers.createCommandResult(setOf(),
                Helpers.createPlayerState(location = Point(4, 4),
                        facing = Direction.NORTH))))
    }

    @Test
    fun `move forward at beginning until danger met`() {
        for (i in 0..3) {
            assertEquals(MoveCommand(),
                    intelligence.chooseNextMove(world, Helpers.createCommandResult(setOf(),
                                    Helpers.createPlayerState(location = Point(0, i)))))
        }
        assertNotEquals(MoveCommand(),
                intelligence.chooseNextMove(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4)))))
    }

    @Test
    fun `get turn count`() {
        val playerState = Helpers.createPlayerState(location = Point(0, 5),
                facing = Direction.NORTH)
        assertEquals(0, intelligence.getTurnCount(playerState, Point(0, 5).north()))
        assertEquals(1, intelligence.getTurnCount(playerState, Point(0, 5).east()))
        assertEquals(2, intelligence.getTurnCount(playerState, Point(0, 5).south()))
        assertEquals(1, intelligence.getTurnCount(playerState, Point(0, 5).west()))
        assertEquals(null, intelligence.getTurnCount(playerState, Point(0, 5).northEast()))
    }

    @Test
    fun `can move in direction`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.WALL_BUMP),
                Helpers.createPlayerState(location = Point(4, 4))))
        assertFalse(intelligence.canMoveInDirection(Direction.NORTH))
        assertTrue(intelligence.canMoveInDirection(Direction.EAST))
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(4, 3))))
        assertFalse(intelligence.canMoveInDirection(Direction.WEST))
        assertTrue(intelligence.canMoveInDirection(Direction.NORTH))
    }

    @Test
    fun `convert target room to command`() {
        assertEquals(MoveCommand(), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(4, 5)))
        assertEquals(TurnCommand(Direction.EAST), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(5, 4)))
        assertEquals(TurnCommand(Direction.SOUTH), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(4, 3)))
        assertEquals(TurnCommand(Direction.WEST), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(3, 4)))
        assertEquals(MoveCommand(), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(0, 0)))
    assertEquals(MoveCommand(), intelligence.toCommand(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                null))
    }
}