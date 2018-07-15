package server

import util.JsonParser.Companion.buildFromJson
import world.World

class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    data class Data(val x: Int, val y: Int, val content: List<String>)

    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        buildFromJson(fileName)
    }

    fun getWorldSize(): Int {
        return world.size
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}

