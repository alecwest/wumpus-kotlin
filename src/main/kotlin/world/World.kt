package world

import world.effect.*
import java.awt.Point

/**
 * Notes:
 * Both the server and the client should be able to utilize one world class
 *
 * When the game starts, the server creates a world and fills in its contents either via a file
 * or procedural generation. The world info will include the following:
 *      Size of the world (one digit, since the world will be square)
 *      List of coordinates for each type of obstacle (no particular order needed), that don't go out of bounds.
 *          Coordinates start at (0,0) at the bottom left, like any graph
 *      Coordinate for player to start at. This point will have no obstacles or content in it.
 *
 * The client, on the other hand, will attempt to establish itself as a player in this new world by contacting the
 * server. Upon success, the client creates a new world for itself with only knowledge of the following:
 *      Size and starting location on map is unknown.
 *          Map fully "exists" but client doesn't know rooms outside of the starting room
 *          For visual effect, rooms that are not yet visited will not be printed.
 *      Dimensions of the world are always square
 *      The starting space is guaranteed safe
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
 *
 * Start:
 *      1. Server creates empty world (initialized to one room)
 *      2. Server reads size number from file and sets size
 *      3. Server reads coordinates of room and the list of RoomContents assigned to that room
 *      4. Server adds each RoomContent to room with given coordinates
 *      5. World updates room and any adjacent rooms that should have related content
 *          A. Each RoomContent has an associated list of WorldEffects (None, AddAdjacent, AddDiagonal, AddOn)
 *          B. Each WorldEffect implements a method applyEffect(x, y, world) that returns a world with the effect
 *          applied appropriately around/on the coordinates
 *          C. Rooms that are not yet visited are still updated. That way when the client considers that room as an option,
 *          they already know about any effects that have been applied to that room
 */
class World(private val size: Int) {
    var rooms: ArrayList<Room> = arrayListOf()

    init {
        for (i in 0..(size * size - 1)){
            rooms.add(Room(arrayListOf()))
        }
    }

    fun addRoomContent(point: Point, content: RoomContent) {
        rooms[getRoomIndex(point)].addRoomContent(content)
        addWorldEffects(point, getAssociatedWorldEffects(content))
    }

    private fun addWorldEffects(point: Point, worldEffects: ArrayList<WorldEffect>) {
        for (worldEffect in worldEffects) {
            worldEffect.applyEffect(this, point)
        }
    }

    fun removeRoomContent(point: Point, content: RoomContent) {
        rooms[getRoomIndex(point)].removeRoomContent(content)
    }

    fun hasRoomContent(point: Point, content: RoomContent): Boolean {
        return rooms[getRoomIndex(point)].hasRoomContent(content)
    }

    fun roomIsEmpty(point: Point): Boolean {
        return rooms[getRoomIndex(point)].isEmpty()
    }

    fun getRoomIndex(point: Point): Int {
        var result = point.y * size + point.x
        if (result > size * size - 1) {
            result = -1
        }
        return result
    }

    private fun getAssociatedWorldEffects(roomContent: RoomContent): ArrayList<WorldEffect> {
        return when(roomContent) {
            RoomContent.BLOCKADE -> arrayListOf(NoEffect())
            RoomContent.BREEZE -> arrayListOf(NoEffect())
            RoomContent.BUMP -> arrayListOf(NoEffect())
            RoomContent.FOOD -> arrayListOf(NoEffect())
            RoomContent.GLITTER -> arrayListOf(NoEffect())
            RoomContent.GOLD -> arrayListOf(AddHereEffect(RoomContent.GLITTER))
            RoomContent.MOO -> arrayListOf(NoEffect())
            RoomContent.PIT -> arrayListOf(AddAdjacentEffect(RoomContent.BREEZE))
            RoomContent.STENCH -> arrayListOf(NoEffect())
            RoomContent.SUPMUW_EVIL -> arrayListOf(AddAdjacentEffect(RoomContent.MOO),
                    AddDiagonalEffect(RoomContent.MOO))
            RoomContent.SUPMUW -> arrayListOf(AddAdjacentEffect(RoomContent.MOO),
                    AddDiagonalEffect(RoomContent.MOO), AddHereEffect(RoomContent.FOOD))
            RoomContent.WUMPUS -> arrayListOf(AddAdjacentEffect(RoomContent.STENCH))
        }
    }
}
