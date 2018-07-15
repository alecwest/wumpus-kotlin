package world

import org.junit.jupiter.api.Assertions.assertEquals

class Util {
    companion object {
        fun createRoom(roomContent: ArrayList<RoomContent>
                       = arrayListOf(RoomContent.BREEZE, RoomContent.STENCH)): Room {
            return Room(roomContent)
        }

        fun assertContains(content: String, subString: String, numExpected: Int) {
            val numFound = content.sumBy {
                if (subString.contains(it))
                    1
                else
                    0
            }
            assertEquals(numExpected, numFound)
        }
    }
}
