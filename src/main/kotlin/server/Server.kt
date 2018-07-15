package server

import world.World

class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        World(worldSize)
    }

    fun getWorldSize(): Int {
        return world.size
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}