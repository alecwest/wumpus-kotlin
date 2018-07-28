package game.world

import java.awt.Point
import java.util.logging.Logger
import kotlin.math.sqrt

data class WorldState(private val rooms: ArrayList<Room> = arrayListOf()) {
    private val log = Logger.getLogger(WorldState::class.qualifiedName)
    private val size = sqrt(rooms.size.toFloat()).toInt()

    fun getRooms() = rooms

    fun getRoomContent(point: Point) = rooms[getRoomIndex(point)].getRoomContent()

    fun hasRoomContent(point: Point, content: RoomContent): Boolean {
        return try {
            rooms[getRoomIndex(point)].hasRoomContent(content)
        } catch (e: ArrayIndexOutOfBoundsException) {
            log.info("Content %s cannot exist in out-of-bounds room.".format(content))
            false
        }
    }

    fun roomIsValid(point: Point): Boolean {
        return try {
            rooms[getRoomIndex(point)]
            true
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    fun roomIsEmpty(point: Point): Boolean {
        return try {
            rooms[getRoomIndex(point)].isEmpty()
        } catch (e: ArrayIndexOutOfBoundsException) {
            log.info("Out of bounds room is empty.")
            true
        }
    }

    fun getRoomIndex(point: Point): Int {
        var result = point.y * size + point.x
        if (point.x < 0 || point.y < 0 || point.x >= size || result > size * size - 1) {
            result = -1
        }
        return result
    }

    fun getWorldMap(): String {
        val result: MutableList<String> = mutableListOf()
        var row: MutableList<String> = mutableListOf()
        for (i in getNumberRooms() - 1 downTo 0) {
            val splitSmallRoomString = rooms[i].getSmallRoomString().split("\n")
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

    fun getRoom(point: Point): Room {
        return rooms[getRoomIndex(point)]
    }

    fun getNumberRooms(): Int {
        return rooms.size
    }

    fun getAmountOfContentInRoom(point: Point): Int {
        val room = getRoom(point)
        return room.getAmountOfContent()
    }

    fun copyThis(rooms: ArrayList<Room> = arrayListOf()) =
            WorldState(rooms)
}
