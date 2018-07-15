package server

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import world.World
import world.toRoomContent
import java.awt.Point
import java.io.FileReader

class Server(private val fileName: String = "", private val worldSize: Int = 10) {
    data class Data(val x: Int, val y: Int, val content: List<String>)

    private val world: World = if (fileName.isBlank()) {
        World(worldSize)
    } else {
        buildFromJson()
    }

    private fun buildFromJson(): World {
        val klaxon = Klaxon()
        var world = World(0)

        JsonReader(FileReader("D:/dev/text-games/wumpus-kotlin/src/test/resources" + fileName)).use { reader ->
            reader.beginObject {
                while (reader.hasNext()) {
                    val readName = reader.nextName()
                    when (readName) {
                        "world-size" -> world = World(reader.nextInt())
                        "data" -> parseDataArray(klaxon, reader, world)
                    }
                }
            }
        }
        return world
    }

    private fun parseDataArray(klaxon: Klaxon, reader: JsonReader, world: World) {
        reader.beginArray {
            while (reader.hasNext()) {
                val data = klaxon.parse<Data>(reader)
                if (data != null) {
                    for (contentChar in data.content) {
                        val roomContent = contentChar.toRoomContent()
                        world.addRoomContent(Point(data.x, data.y), roomContent)
                    }
                }
            }
        }
    }

    fun getWorldSize(): Int {
        return world.size
    }

    fun getNumberRooms(): Int {
        return world.getNumberRooms()
    }
}

