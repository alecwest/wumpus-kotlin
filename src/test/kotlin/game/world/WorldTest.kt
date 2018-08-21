package game.world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import Helpers.Companion.assertContains
import Helpers.Companion.createWorld
import game.player.PlayerState
import game.world.GameObjectFeature.*
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
        assertEquals(setOf<GameObject>(), world.getGameObjects(point))
        world.addGameObject(point, GameObject.BLOCKADE)
        assertEquals(setOf(GameObject.BLOCKADE), world.getGameObjects(point))
    }

    @Test
    fun `get out of bounds room content`() {
        assertEquals(setOf<GameObject>(), world.getGameObjects(Point(-1, 3)))
        assertEquals(setOf<GameObject>(), world.getGameObjects(Point(12, 3)))
    }

    companion object {
        @JvmStatic
        fun validAddGameObjectProvider() = Stream.of(
                ValidGameObjectTestData(setOf(), arrayListOf(GameObject.GOLD), arrayListOf(GameObject.GOLD, GameObject.GLITTER)),
                ValidGameObjectTestData(setOf(), arrayListOf(GameObject.MOO), arrayListOf(GameObject.MOO)),
                ValidGameObjectTestData(setOf(), arrayListOf(GameObject.ARROW), arrayListOf(GameObject.ARROW))
        )

        @JvmStatic
        fun validRemoveGameObjectProvider() = Stream.of(
                ValidGameObjectTestData(setOf(GameObject.PIT), arrayListOf(GameObject.PIT), arrayListOf()),
                ValidGameObjectTestData(setOf(GameObject.GOLD, GameObject.GLITTER), arrayListOf(GameObject.GOLD), arrayListOf()),
                ValidGameObjectTestData(setOf(GameObject.ARROW, GameObject.WUMPUS), arrayListOf(GameObject.ARROW), arrayListOf(GameObject.WUMPUS))
        )

        @JvmStatic
        fun validSimilarGameObjectWithEffectsProvider() = Stream.of(
                ValidSimilarGameObjectWithEffectsTestData(GameObject.PIT, GameObject.BREEZE,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).northEast().north(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4).north(), Point(4, 4).east(), Point(4, 4).south(), Point(4, 4).northEast())),
                ValidSimilarGameObjectWithEffectsTestData(GameObject.WUMPUS, GameObject.STENCH,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4).north(), Point(4, 4).east(), Point(4, 4).south())),
                ValidSimilarGameObjectWithEffectsTestData(GameObject.SUPMUW, GameObject.MOO,
                        arrayListOf(Point(4, 4), Point(4, 4).northEast(), Point(4, 4).south().south()),
                        Point(4, 4),
                        arrayListOf(Point(4, 4), Point(4, 4).north(), Point(4, 4).east(),
                                Point(4, 4).southWest(), Point(4, 4).south(), Point(4, 4).southEast()))
        )

    }

    @ParameterizedTest
    @MethodSource("validAddGameObjectProvider")
    fun `add content to room`(testData: ValidGameObjectTestData) {
        for (gameObject in testData.contentToAddOrRemove) {
            world.addGameObject(Point(1, 1), gameObject)
        }
        assertEquals(testData.finalContent.size, world.getAmountOfObjectsInRoom(Point(1, 1)))
        for (gameObject in testData.finalContent) {
            assertTrue(world.hasGameObject(Point(1, 1), gameObject))
        }
    }

    @Test
    fun `add content to out-of-bounds room`() {
        world.addGameObject(Point(-1, 1), GameObject.STENCH)
        world.addGameObject(Point(2, -4), GameObject.SUPMUW)
    }

    @ParameterizedTest
    @MethodSource("validRemoveGameObjectProvider")
    fun `remove content from room and make sure effects remain where needed`(testData: ValidGameObjectTestData) {
        val pointToRemove = Point(1, 1)
        val world = Helpers.createWorld(gameObject = mapOf(pointToRemove to testData.initialContent))
        for (gameObject in testData.contentToAddOrRemove) {
            world.removeGameObject(pointToRemove, gameObject)
        }
        assertEquals(testData.finalContent.size, world.getAmountOfObjectsInRoom(pointToRemove))
        for (gameObject in testData.finalContent) {
            assertTrue(world.hasGameObject(pointToRemove, gameObject))
        }
    }

    @ParameterizedTest
    @MethodSource("validSimilarGameObjectWithEffectsProvider")
    fun `ensure world effects stay on remove if nearby rooms contain associated content`(testData: ValidSimilarGameObjectWithEffectsTestData) {
        val world = Helpers.createWorld()
        for (point in testData.locationsOfContent) {
            world.addGameObject(point, testData.contentTested)
        }

        world.removeGameObject(testData.locationToRemoveContent, testData.contentTested)

        for (point in testData.locationsThatShouldStillHaveContentEffect) {
            assertTrue(world.hasGameObject(point, testData.effectTested))
        }
    }

    @Test
    fun `remove content from out-of-bounds room`() {
        world.removeGameObject(Point(-1, 1), GameObject.STENCH)
        world.removeGameObject(Point(2, -4), GameObject.SUPMUW)
    }

    @Test
    fun `check room for content`() {
        world.addGameObject(Point(1, 1), GameObject.BREEZE)
        world.addGameObject(Point(1, 1), GameObject.STENCH)

        assertTrue(world.hasGameObject(Point(1, 1), GameObject.BREEZE))
        assertFalse(world.hasGameObject(Point(1, 1), GameObject.FOOD))
    }

    @Test
    fun `check out-of-bounds room for content`() {
        assertFalse(world.hasGameObject(Point(-1, 1), GameObject.STENCH))
        assertFalse(world.hasGameObject(Point(2, -4), GameObject.SUPMUW))
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
        world.addGameObject(Point(1, 1), GameObject.BREEZE)
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
        world.addGameObject(Point(2, 2), GameObject.PIT)
        val worldMap = world.getWorldMap()
        val target1 = (GameObject.PIT.getFeature(Mappable()) as Mappable).character
        val target2 = (GameObject.BREEZE.getFeature(Mappable()) as Mappable).character
        assertContains(worldMap, target1, 1)
        assertContains(worldMap, target2, 4)
    }

    @Test
    fun `check for room content on world map edge`() {
        world.addGameObject(Point(0, 0), GameObject.SUPMUW)
        val worldMap = world.getWorldMap()
        val target1 = (GameObject.SUPMUW.getFeature(Mappable()) as Mappable).character
        val target2 = (GameObject.MOO.getFeature(Mappable()) as Mappable).character
        assertContains(worldMap, target1, 1)
        assertContains(worldMap, target2, 3)
    }

    @Test
    fun `check for room content when added out of bounds`() {
        world.addGameObject(Point(-1,0), GameObject.WUMPUS)
        val worldMap = world.getWorldMap()
        val target1 = (GameObject.WUMPUS.getFeature(Mappable()) as Mappable).character
        val target2 = (GameObject.STENCH.getFeature(Mappable()) as Mappable).character
        assertContains(worldMap, target1, 0)
        assertContains(worldMap, target2, 0)
    }

    @Test
    fun `check for player when PlayerState added`() {
        val worldMap = world.getWorldMap(playerState = PlayerState(facing = Direction.EAST))
        assertContains(worldMap, Direction.EAST.toPlayerMapRepresentation(), 1)
    }
}

data class ValidGameObjectTestData (
        val initialContent: Set<GameObject>,
        val contentToAddOrRemove: ArrayList<GameObject>,
        val finalContent: ArrayList<GameObject>
)

data class ValidSimilarGameObjectWithEffectsTestData (
        val contentTested: GameObject,
        val effectTested: GameObject,
        val locationsOfContent: ArrayList<Point>,
        val locationToRemoveContent: Point,
        val locationsThatShouldStillHaveContentEffect: ArrayList<Point>
)
