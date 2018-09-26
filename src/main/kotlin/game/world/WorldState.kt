package game.world

import game.player.PlayerState
import util.Direction
import java.awt.Point
import kotlin.math.sqrt

data class WorldState(private val rooms: ArrayList<Room> = arrayListOf()) {
    private val size = sqrt(rooms.size.toFloat()).toInt()

    /**
     * @return [List] all rooms in the world
     */
    fun getRooms() = rooms

    /**
     * @param point room to get objects from
     *
     * @return [Set] of all game objects in the room
     */
    fun getGameObjects(point: Point) = when {
        roomIsValid(point) -> rooms[getRoomIndex(point)].getGameObjects()
        else -> setOf()
    }

    /**
     * @param point room to check
     * @param content object to check for
     *
     * @return [Boolean] indicating room has object
     */
    fun hasGameObject(point: Point, content: GameObject): Boolean {
        return try {
            rooms[getRoomIndex(point)].hasGameObject(content)
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room is within world boundaries
     */
    fun roomIsValid(point: Point): Boolean {
        return try {
            rooms[getRoomIndex(point)]
            true
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating no objects in room
     */
    fun roomIsEmpty(point: Point): Boolean {
        return try {
            rooms[getRoomIndex(point)].isEmpty()
        } catch (e: ArrayIndexOutOfBoundsException) {
            true
        }
    }

    /**
     * @param point room to check
     *
     * @return [Int] indicating array index of room
     */
    fun getRoomIndex(point: Point): Int {
        var result = point.y * size + point.x
        if (point.x < 0 || point.y < 0 || point.x >= size || result > size * size - 1) {
            result = -1
        }
        return result
    }

    /**
     * @param index array index of room
     *
     * @return [Point] location of room
     */
    fun getRoomPoint(index: Int): Point {
        var result = Point(index % size, index / size)
        if (index < 0 || index >= getNumberRooms()) {
            result = Point(-1, -1)
        }
        return result
    }

    /**
     * @param playerState optional state of the player
     *
     * @return [String] 2-Dimensional map of the world
     */
    fun getWorldMap(playerState: PlayerState? = null): String {
        val result: MutableList<String> = mutableListOf()
        var row: MutableList<String> = mutableListOf()
        for (i in getNumberRooms() - 1 downTo 0) {
            val splitSmallRoomString = rooms[i].getSmallRoomString(
                    playerDirectionIfInRoom(i, playerState)).split("\n")
                    .toMutableList()
            when {
                i % size == size - 1 -> row = splitSmallRoomString
                else -> {
                    for (j in 0 until splitSmallRoomString.size) {
                        row[j] = splitSmallRoomString[j] + row[j]
                    }
                    if (i % size == 0) {
                        result.add(row.joinToString(separator = "\n"))
                    }
                }
            }
        }
        return result.joinToString(separator = "\n")
    }

    /**
     * @param index room index
     * @param playerState state of player
     *
     * @return [Direction] indicating which way the player is facing in the room, or null if they aren't in the room
     */
    private fun playerDirectionIfInRoom(index: Int, playerState: PlayerState?): Direction? {
        if (playerState == null) return null
        if (getRoomIndex(playerState.getLocation()) == index) {
            return playerState.getDirection()
        }
        return null
    }

    /**
     * @param point room to get
     *
     * @return [Room]
     */
    fun getRoom(point: Point): Room {
        return rooms[getRoomIndex(point)]
    }

    /**
     * @return [Int] total number of rooms in world
     */
    fun getNumberRooms(): Int {
        return rooms.size
    }

    /**
     * @param point room to check
     *
     * @return [Int] number of objects in the room
     */
    fun getAmountOfObjectsInRoom(point: Point): Int {
        val room = getRoom(point)
        return room.getAmountOfObjects()
    }

    /**
     * @param rooms list of rooms
     *
     * @return [WorldState] deep-copy of this WorldState
     */
    fun copyThis(rooms: ArrayList<Room> = this.rooms) =
            WorldState(rooms)
}
