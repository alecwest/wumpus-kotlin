package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import Helpers.Companion.assertContains
import Helpers.Companion.createWorld
import util.*
import java.awt.Point
import java.util.stream.Stream

// TODO use @ParameterizedTest and @MethodSource
class WorldTest {
    private val world: World = World(4)

    @Test
    fun `verify world init`() {
        val world2 = World(3)
        assertEquals(world.getSize() * world.getSize(), world.getNumberRooms())
        assertEquals(world2.getSize() * world2.getSize(), world2.getNumberRooms())
    }

    @Test
    fun `check given world size is equal on get`() {
        assertEquals(4, world.getSize())
        val world = createWorld(size = 5)
        assertEquals(5, world.getSize())
    }

    @Test
    fun `get room content`() {
        val point = Point(0, 0)
        assertEquals(arrayListOf<RoomContent>(), world.getRoomContent(point))
        world.addRoomContent(point, RoomContent.BLOCKADE)
        assertEquals(arrayListOf(RoomContent.BLOCKADE), world.getRoomContent(point))
    }

    @Test
    fun `get out of bounds room content`() {
        assertEquals(arrayListOf<RoomContent>(), world.getRoomContent(Point(-1, 3)))
        assertEquals(arrayListOf<RoomContent>(), world.getRoomContent(Point(12, 3)))
    }

    companion object {
        @JvmStatic
        fun validAddRoomContentProvider() = Stream.of(
                ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.GOLD), arrayListOf(RoomContent.GOLD, RoomContent.GLITTER)),
                ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.MOO), arrayListOf(RoomContent.MOO)),
                ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.ARROW), arrayListOf(RoomContent.ARROW))
        )

        @JvmStatic
        fun validRemoveRoomContentProvider() = Stream.of(
                ValidRoomContentTestData(arrayListOf(RoomContent.PIT), arrayListOf(RoomContent.PIT), arrayListOf()),
                ValidRoomContentTestData(arrayListOf(RoomContent.GOLD, RoomContent.GLITTER), arrayListOf(RoomContent.GOLD), arrayListOf()),
                ValidRoomContentTestData(arrayListOf(RoomContent.ARROW, RoomContent.WUMPUS), arrayListOf(RoomContent.ARROW), arrayListOf(RoomContent.WUMPUS))
        )

        @JvmStatic
        fun validSimilarRoomContentWithEffectsProvider() = Stream.of(
                ValidSimilarRoomContentWithEffectsTestData(RoomContent.PIT, RoomContent.BREEZE,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).northEast().north(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4).north(), Point(4, 4).east(), Point(4, 4).south(), Point(4, 4).northEast())),
                ValidSimilarRoomContentWithEffectsTestData(RoomContent.WUMPUS, RoomContent.STENCH,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4).north(), Point(4, 4).east(), Point(4, 4).south())),
                ValidSimilarRoomContentWithEffectsTestData(RoomContent.SUPMUW, RoomContent.MOO,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4), Point(4, 4).north(), Point(4, 4).east(),
                                Point(4, 4).southWest(), Point(4, 4).south(), Point(4, 4).southEast()))
        )

    }

    @ParameterizedTest
    @MethodSource("validAddRoomContentProvider")
    fun `add content to room`(testData: ValidRoomContentTestData) {
        for (roomContent in testData.contentToAddOrRemove) {
            world.addRoomContent(Point(1, 1), roomContent)
        }
        assertEquals(testData.finalContent.size, world.getAmountOfContentInRoom(Point(1, 1)))
        for (roomContent in testData.finalContent) {
            assertTrue(world.hasRoomContent(Point(1, 1), roomContent))
        }
    }

    @Test
    fun `add content to out-of-bounds room`() {
        world.addRoomContent(Point(-1, 1), RoomContent.STENCH)
        world.addRoomContent(Point(2, -4), RoomContent.SUPMUW)
    }

    @ParameterizedTest
    @MethodSource("validRemoveRoomContentProvider")
    fun `remove content from room and make sure effects remain where needed`(testData: ValidRoomContentTestData) {
        val pointToRemove = Point(1, 1)
        val world = Helpers.createWorld(roomContent = mapOf(pointToRemove to testData.initialContent))
        for (roomContent in testData.contentToAddOrRemove) {
            world.removeRoomContent(pointToRemove, roomContent)
        }
        assertEquals(testData.finalContent.size, world.getAmountOfContentInRoom(pointToRemove))
        for (roomContent in testData.finalContent) {
            assertTrue(world.hasRoomContent(pointToRemove, roomContent))
        }
    }

    @ParameterizedTest
    @MethodSource("validSimilarRoomContentWithEffectsProvider")
    fun `ensure world effects stay on remove if nearby rooms contain associated content`(testData: ValidSimilarRoomContentWithEffectsTestData) {
        val world = Helpers.createWorld()
        for (point in testData.locationsOfContent) {
            world.addRoomContent(point, testData.contentTested)
        }

        world.removeRoomContent(testData.locationToRemoveContent, testData.contentTested)

        for (point in testData.locationsThatShouldStillHaveContentEffect) {
            assertTrue(world.hasRoomContent(point, testData.effectTested))
        }
    }

    @Test
    fun `remove content from out-of-bounds room`() {
        world.removeRoomContent(Point(-1, 1), RoomContent.STENCH)
        world.removeRoomContent(Point(2, -4), RoomContent.SUPMUW)
    }

    @Test
    fun `check room for content`() {
        world.addRoomContent(Point(1, 1), RoomContent.BREEZE)
        world.addRoomContent(Point(1, 1), RoomContent.STENCH)

        assertTrue(world.hasRoomContent(Point(1, 1), RoomContent.BREEZE))
        assertFalse(world.hasRoomContent(Point(1, 1), RoomContent.FOOD))
    }

    @Test
    fun `check out-of-bounds room for content`() {
        assertFalse(world.hasRoomContent(Point(-1, 1), RoomContent.STENCH))
        assertFalse(world.hasRoomContent(Point(2, -4), RoomContent.SUPMUW))
    }

    @Test
    fun `check room is valid`() {
        assertTrue(world.roomIsValid(Point(0, 0)))
        assertFalse(world.roomIsValid(Point(-1, 0)))
        assertFalse(world.roomIsValid(Point(0, 4)))
        assertFalse(world.roomIsValid(Point(5, 3)))
    }

    @Test
    fun `check room is empty`() {
        world.addRoomContent(Point(1, 1), RoomContent.BREEZE)
        assertTrue(world.roomIsEmpty(Point(0, 0)))
        assertFalse(world.roomIsEmpty(Point(1, 1)))
    }

    @Test
    fun `check out-of-bounds room is empty`() {
        assertTrue(world.roomIsEmpty(Point(-1, 1)))
        assertTrue(world.roomIsEmpty(Point(2, -4)))
    }

    @Test
    fun `get index of room`() {
        assertEquals(0, world.getRoomIndex(Point(0, 0)))
        assertEquals(1, world.getRoomIndex(Point(1, 0)))
        assertEquals(4, world.getRoomIndex(Point(0, 1)))
        assertEquals(5, world.getRoomIndex(Point(1, 1)))
    }

    @Test
    fun `get out of range indices`() {
        assertEquals(-1, world.getRoomIndex(Point(4, 1)))
        assertEquals(-1, world.getRoomIndex(Point(1, 4)))
        assertEquals(-1, world.getRoomIndex(Point(-1, 4)))
        assertEquals(-1, world.getRoomIndex(Point(1, -1)))
    }

    @Test
    fun `ensure exactly size * 5 lines in world map string`() {
        assertEquals(world.getSize() * 5, world.getWorldMap()
                .split("""([^\n]*\n)""".toRegex()).size)
    }

    @Test
    fun `check for room content in world map`() {
        world.addRoomContent(Point(2, 2), RoomContent.PIT)
        val worldMap = world.getWorldMap()
        val target1 = RoomContent.PIT.toCharRepresentation()
        val target2 = RoomContent.BREEZE.toCharRepresentation()
        assertContains(worldMap, target1, 1)
        assertContains(worldMap, target2, 4)
    }

    @Test
    fun `check for room content on world map edge`() {
        world.addRoomContent(Point(0, 0), RoomContent.SUPMUW_EVIL)
        val worldMap = world.getWorldMap()
        val target1 = RoomContent.SUPMUW_EVIL.toCharRepresentation()
        val target2 = RoomContent.MOO.toCharRepresentation()
        assertContains(worldMap, target1, 1)
        assertContains(worldMap, target2, 3)
    }

    @Test
    fun `check for room content when added out of bounds`() {
        world.addRoomContent(Point(-1,0), RoomContent.WUMPUS)
        val worldMap = world.getWorldMap()
        val target1 = RoomContent.WUMPUS.toCharRepresentation()
        val target2 = RoomContent.STENCH.toCharRepresentation()
        assertContains(worldMap, target1, 0)
        assertContains(worldMap, target2, 0)
    }
}

data class ValidRoomContentTestData (
        val initialContent: ArrayList<RoomContent>,
        val contentToAddOrRemove: ArrayList<RoomContent>,
        val finalContent: ArrayList<RoomContent>
)

data class ValidSimilarRoomContentWithEffectsTestData (
        val contentTested: RoomContent,
        val effectTested: RoomContent,
        val locationsOfContent: ArrayList<Point>,
        val locationToRemoveContent: Point,
        val locationsThatShouldStillHaveContentEffect: ArrayList<Point>
)
