package game.agent.intelligence

import game.agent.intelligence.Answer.*
import game.agent.intelligence.Fact.*
import game.command.ExitCommand
import game.command.GrabCommand
import game.command.MoveCommand
import game.command.TurnCommand
import game.player.InventoryItem
import game.world.GameObject
import game.world.GameObject.*
import game.world.Perception
import game.world.gameObjectValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.*
import java.awt.Point

internal class KnowledgeBasedIntelligenceTest {
    val intelligence = KnowledgeBasedIntelligence()
    val world = Helpers.createWorld()

    init {
        intelligence.world = world
        intelligence.commandResult = Helpers.createCommandResult()
    }

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
        assertEquals(listOf(MoveCommand()), intelligence.chooseNextMoves(world, Helpers.createCommandResult()))
    }

    @Test
    fun `move back when danger is met`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(), Helpers.createPlayerState(location = Point(7, 2),
                facing = Direction.EAST)))
        assertEquals(listOf(TurnCommand(Direction.WEST), MoveCommand(), MoveCommand()), intelligence.chooseNextMoves(world,
                Helpers.createCommandResult(setOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(8, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `move to the safe room on right or left when danger is met if it exists`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(2, 1))))
        assertEquals(listOf(TurnCommand(Direction.SOUTH), MoveCommand(), MoveCommand()), intelligence.chooseNextMoves(world,
                Helpers.createCommandResult(setOf(Perception.STENCH),
                        Helpers.createPlayerState(location = Point(2, 2),
                                facing = Direction.EAST))))
    }

    @Test
    fun `go forward when danger is met if forward facing room is known to be safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(2, 2))))
        assertEquals(listOf(MoveCommand(), MoveCommand()), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `do not turn around when danger is met if other nearby room is known to be safe`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                setOf(),
                Helpers.createPlayerState(location = Point(3, 8))))
        assertNotEquals(listOf(TurnCommand(Direction.WEST)), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
                setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(3, 9), facing = Direction.EAST))))
    }

    @Test
    fun `grab item in room`() {
        assertEquals(listOf(GrabCommand(InventoryItem.GOLD)), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.STENCH, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
    }

    @Test
    fun `leave with gold in inventory`() {
        assertEquals(listOf(ExitCommand()), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
                setOf(Perception.EXIT), Helpers.createPlayerState(inventoryContent = mapOf(InventoryItem.GOLD to 1)))))
    }

    @Test
    fun `grab multiple items in room`() {
        assertEquals(listOf(GrabCommand(InventoryItem.FOOD)), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
                setOf(Perception.GLITTER, Perception.STENCH, Perception.FOOD, Perception.BREEZE),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.NORTH))))
        assertEquals(listOf(GrabCommand(InventoryItem.GOLD)), intelligence.chooseNextMoves(world, Helpers.createCommandResult(
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
    fun `move away from blocking rooms`() {
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(4, 5))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(5, 4))))
        assertEquals(listOf(TurnCommand(Direction.EAST), MoveCommand()),
                intelligence.chooseNextMoves(world, Helpers.createCommandResult(
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
        assertNotEquals(listOf(TurnCommand(Direction.SOUTH)),
                intelligence.chooseNextMoves(world, Helpers.createCommandResult(setOf(),
                Helpers.createPlayerState(location = Point(4, 4),
                        facing = Direction.NORTH))))
    }

    @Test
    fun `move forward at beginning until danger met`() {
        for (i in 0..3) {
            assertEquals(listOf(MoveCommand()),
                    intelligence.chooseNextMoves(world, Helpers.createCommandResult(setOf(),
                                    Helpers.createPlayerState(location = Point(0, i)))))
        }
        assertNotEquals(listOf(MoveCommand()),
                intelligence.chooseNextMoves(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(0, 4)))))
    }

    @Test
    fun `convert target room to command`() {
        assertEquals(listOf(MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(4, 5)))
        assertEquals(listOf(TurnCommand(Direction.EAST), MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(5, 4)))
        assertEquals(listOf(TurnCommand(Direction.SOUTH), MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(4, 3)))
        assertEquals(listOf(TurnCommand(Direction.WEST), MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(3, 4)))
        assertEquals(listOf(MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                Point(0, 0)))
    assertEquals(listOf(MoveCommand()), intelligence.toCommands(
                Helpers.createPlayerState(location = Point(4, 4), facing = Direction.NORTH),
                null))
    }

    @Test
    fun `get best explorative moves`() {
        processSafeRoom(Point(4, 3))
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                Helpers.createPlayerState(location = Point(4, 4))))

        assertEquals(listOf(TurnCommand(Direction.SOUTH), MoveCommand(), MoveCommand()), intelligence.bestExplorativeMoves(intelligence.commandResult.getPlayerState()))
        assertEquals(listOf(MoveCommand(), MoveCommand()), intelligence.bestExplorativeMoves(Helpers.createPlayerState(
                location = Point(4, 4), facing = Direction.SOUTH)))
    }

    @Test
    fun `get best explorative moves when surrounded by already known rooms`() {
        for (i in 2 downTo 0) for (j in 2 downTo 0) processSafeRoom(Point(i, j))
        processSafeRoom(Point(1, 1)) // Set player location to (1, 1)
        assertEquals(listOf(MoveCommand(), MoveCommand()), intelligence.bestExplorativeMoves(intelligence.commandResult.getPlayerState()))
    }

    @Test
    fun `get path to room`() {
        for (i in 2 downTo 0) {
            for (j in 2 downTo 0) {
                processSafeRoom(Point(i, j))
            }
        }
        assertEquals(setOf(Point(0, 1), Point(0, 2), Point(1, 2), Point(2, 2)),
                intelligence.pathToRoom(setOf(Point(2, 2))))
    }

    @Test
    fun `get safe path to room`() {
        // Simulate existence of pit at (0, 2)
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(1, 0))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(2, 0))))
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                playerState = Helpers.createPlayerState(location = Point(0, 1))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(1, 1))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(2, 1))))
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BREEZE),
                playerState = Helpers.createPlayerState(location = Point(1, 2))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(2, 2))))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = Helpers.createPlayerState(location = Point(0, 0))))
        assertEquals(setOf(Point(0, 1), Point(1, 1), Point(2, 1), Point(2, 2)),
                intelligence.pathToRoom(setOf(Point(2, 2))))
    }

    @Test
    fun `get cost of move to room`() {
        assertEquals(2, intelligence.costOfMoveToRoom(Point(0, 1), Point(0, 0), Direction.EAST))
        assertEquals(3, intelligence.costOfMoveToRoom(Point(0, 1), Point(0, 0), Direction.SOUTH))
        assertEquals(1, intelligence.costOfMoveToRoom(Point(0, 1), Point(0, 0), Direction.NORTH))
        assertEquals(-1, intelligence.costOfMoveToRoom(Point(0, 1), Point(0, 3), Direction.SOUTH))
        assertEquals(-1, intelligence.costOfMoveToRoom(Point(0, 1), Point(0, 3), null))
    }

    @Test
    fun `get path to closest unknown room`() {
        // (0-2, 1) are safe, (3, 1) has a blockade, (4-9, 1) are safe
        // Simulate blockades at (0-2, 2)
        for (i in 0..2) {
            intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BLOCKADE_BUMP),
                    Helpers.createPlayerState(location = Point(i, 1), facing = Direction.NORTH)))
        }
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(Perception.BLOCKADE_BUMP),
                Helpers.createPlayerState(location = Point(2, 1), facing = Direction.EAST)))
        for (i in 4..9) {
            processSafeRoom(Point(i, 1))
        }
        // (0-9, 0) are all safe
        for (i in 1..9) {
            processSafeRoom(Point(i, 0))
        }
        processSafeRoom(Point(0, 0))
        assertEquals(setOf(Point(1, 0), Point(2, 0), Point(3, 0), Point(4, 0), Point(4, 1), Point(4, 2)),
                intelligence.pathToRoom())
    }

    @Test
    fun `exit world when not in room with exit`() {
        val playerState = Helpers.createPlayerState(
                location = Point(1, 1), facing = Direction.SOUTH,
                inventoryContent = mapOf(InventoryItem.GOLD to 1))
        processSafeRoom(Point(0, 0))
        intelligence.facts.addFact(Point(0, 0), HAS, EXIT)
        processSafeRoom(Point(0, 1))
        processSafeRoom(Point(1, 0))
        intelligence.processLastMove(world, Helpers.createCommandResult(
                playerState = playerState))
        assertEquals(listOf(MoveCommand(), TurnCommand(Direction.WEST), MoveCommand(), ExitCommand()),
                intelligence.exit(playerState))
    }

    private fun processSafeRoom(point: Point) {
        intelligence.processLastMove(world, Helpers.createCommandResult(setOf(),
                Helpers.createPlayerState(location = point, facing = Direction.NORTH)))
    }
}