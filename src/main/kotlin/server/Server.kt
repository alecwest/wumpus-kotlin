package server

import world.World

class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        generateWorldFromFile(fileName)
    }

    private fun generateWorldFromFile(fileName: String): World {
        val worldManifest = this.javaClass.getResource(fileName)
                .readText().split("\n")

        return World(worldManifest[0].toInt())
    }

    fun getWorldSize(): Int {
        return world.size
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}