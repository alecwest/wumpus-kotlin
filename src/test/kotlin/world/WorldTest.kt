package world

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

// TODO use @ParameterizedTest and @MethodSource
class WorldTest {
    @Test
    fun `verify world init`() {
        val world = World(2)
        assertEquals(4, world.rooms.size)
    }


    @ParameterizedTest
    @MethodSource("validAddRoomContentProvider")
    fun `add content to room`(testData: ValidRoomContentTestData) {
        val world = World(2)
        for (roomContent in testData.contentToAddOrRemove) {
            world.addRoomContent(Point(1, 1), roomContent)
        }
        assertEquals(testData.finalContent.size, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
        for (roomContent in testData.finalContent) {
            assertTrue(world.hasRoomContent(Point(1, 1), roomContent))
        }
    }

    private fun validAddRoomContentProvider() = Stream.of(
            ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.GOLD), arrayListOf(RoomContent.GOLD, RoomContent.GLITTER)),
            ValidRoomContentTestData(arrayListOf(), arrayListOf(RoomContent.MOO), arrayListOf(RoomContent.MOO))
    )


    @Test
    fun `remove content from room`() {
        val world = World(2)
        world.addRoomContent(Point(1, 1), RoomContent.BREEZE)
        world.addRoomContent(Point(1, 1), RoomContent.STENCH)

        val initialNumberItems = world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size
        world.removeRoomContent(Point(1, 1), RoomContent.BREEZE)
        assertEquals(initialNumberItems - 1, world.rooms[world.getRoomIndex(Point(1, 1))].roomContent.size)
    }

    @Test
    fun `check room for content`() {
        val world = World(2)
        world.addRoomContent(Point(1, 1), RoomContent.BREEZE)
        world.addRoomContent(Point(1, 1), RoomContent.STENCH)

        assertTrue(world.hasRoomContent(Point(1, 1), RoomContent.BREEZE))
        assertFalse(world.hasRoomContent(Point(1, 1), RoomContent.FOOD))
    }

    @Test
    fun `get index of room`() {
        val world = World(2)
        assertEquals(0, world.getRoomIndex(Point(0, 0)))
        assertEquals(1, world.getRoomIndex(Point(1, 0)))
        assertEquals(2, world.getRoomIndex(Point(0, 1)))
        assertEquals(3, world.getRoomIndex(Point(1, 1)))
        assertEquals(-1, world.getRoomIndex(Point(2, 1)))
    }
}

data class ValidRoomContentTestData (
        val initialContent: ArrayList<RoomContent>,
        val contentToAddOrRemove: ArrayList<RoomContent>,
        val finalContent: ArrayList<RoomContent>
)
