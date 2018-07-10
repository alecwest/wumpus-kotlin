package world

/**
 * Notes:
 * Both the server and the client should be able to utilize one world class
 *
 * When the game starts, the server creates a world and fills in its contents either via a file
 * or procedural generation. The world info will include the following:
 *      Size of the world (one digit, since the main.world will be square)
 *      List of coordinates for each type of obstacle (no particular order needed), that don't go out of bounds.
 *      Coordinate for player to start at. This point will have no obstacles or content in it.
 *
 * The client, on the other hand, will attempt to establish itself as a player in this new world by contacting the
 * server. Upon success, the client creates a new world for itself with only knowledge of the following:
 *      No known size (the map is a 1x1 square)
 *      Dimensions of the world are always square
 *      The starting space is guaranteed safe
 *      NOTE: The client's world will be changing as it discovers new rooms, which means its perception of coordinates
 *      will not align with the server's.
 *
 * The World class simply acts as an aggregate for all known/accumulated knowledge of each room, whether it's for the
 * server or the client. When the player makes a move, they will declare there move to the server, who then queries its
 * own world instance for what exists in the target room. Upon return of this information, the server will pass it on
 * to the client, who then is able to update their own world instance.
 *
 * Storage of room knowledge should be contained in arraylist of rooms. For the server, this is easy. For the client,
 * there may be a frequent need to update the array based on changes in the known size of the map, thus altering
 * their understanding of coordinates for each room. The client will need to make sure they do not lose or damage what
 * knowledge they already have in doing this.
 */
class World(var world: ArrayList<Room>) {
    fun addRoomContent(x: Int, y: Int, content: RoomContent) {
        world[getRoomIndex(x, y)].addRoomContent(content)
    }

    private fun getRoomIndex(x: Int, y: Int): Int {
        return x * getWorldDimension() + y - 1
    }

    private fun getWorldDimension(): Int {
        return Math.sqrt(world.size.toDouble()).toInt()
    }
}