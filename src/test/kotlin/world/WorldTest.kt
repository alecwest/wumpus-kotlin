package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

// TODO use @ParameterizedTest and @MethodSource
class WorldTest {
    private val world: World = World(4)

    @Test
    fun `verify world init`() {
        val world2 = World(3)
        assertEquals(world.size * world.size, world.rooms.size)
        assertEquals(world2.size * world2.size, world2.rooms.size)
    }


    @ParameterizedTest
    @MethodSource("validAddRoomContentProvider")
    fun `add content to room`(testData: ValidRoomContentTestData) {
        for (roomContent in testData.contentToAddOrRemove) {
            world.addRoomContent(Point(1, 1), roomContent)
        }
        assertEquals(testData.finalContent.size, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
        for (roomContent in testData.finalContent) {
            assertTrue(world.hasRoomContent(Point(1, 1), roomContent))
        }
    }

    companion object {
        @JvmStatic
        fun validAddRoomContentProvider() = Stream.of(
                ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.GOLD), arrayListOf(RoomContent.GOLD, RoomContent.GLITTER)),
                ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.MOO), arrayListOf(RoomContent.MOO))
        )
    }

    @Test
    fun `add content to out-of-bounds room`() {
        world.addRoomContent(Point(-1, 1), RoomContent.STENCH)
        world.addRoomContent(Point(2, -4), RoomContent.SUPMUW)
    }

    @Test
    fun `remove content from room`() {
        world.addRoomContent(Point(1, 1), RoomContent.BREEZE)
        world.addRoomContent(Point(1, 1), RoomContent.STENCH)

        val initialNumberItems = world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size
        world.removeRoomContent(Point(1, 1), RoomContent.BREEZE)
        assertEquals(initialNumberItems - 1, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
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
        assertEquals(world.size * 5, world.getWorldMap()
                .split("""([^\n]*\n)""".toRegex()).size)
    }
}

data class ValidRoomContentTestData (
        val initialContent: ArrayList<RoomContent>,
        val contentToAddOrRemove: ArrayList<RoomContent>,
        val finalContent: ArrayList<RoomContent>
)
