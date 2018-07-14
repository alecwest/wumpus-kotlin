package world

/**
 * Some features of the Room that will differ from the c++ implementation:
 *      Rooms cannot know their coordinates. This knowledge shouldn't be passed down to a Room.
 *      Rooms cannot know the world size as well as the number of rooms in the world.
 *      A room only contains information on what exists inside it.
 *      It is up to the client to determine any "status" or "inferences" about any room.
 */
class Room(var roomContent: ArrayList<RoomContent>) {
    fun addRoomContent(content: RoomContent) {
        if (!hasRoomContent(content)) {
            roomContent.add(content)
        }
    }

    fun removeRoomContent(content: RoomContent) {
        if (hasRoomContent(content)) {
            roomContent.remove(content)
        }
    }

    fun hasRoomContent(content: RoomContent): Boolean {
        return roomContent.contains(content)
    }

    fun isEmpty(): Boolean {
        return roomContent.size == 0
    }

    fun getSmallRoomString(): String {
        var roomString: String = """
             -------
            |x x x x|
            |x x x x|
            |x x x x|
             -------""".trimIndent()
        for (content in roomContent) {
            roomString = roomString.replaceFirst(
                    "x", content.toCharRepresentation())
        }
        return roomString.replace("x", " ")
    }
}