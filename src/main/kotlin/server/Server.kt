package server

import util.JsonParser.Companion.buildFromJsonFile
import world.World

class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        buildFromJsonFile(fileName)
    }

    fun getWorldSize(): Int {
        return world.getSize()
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}

