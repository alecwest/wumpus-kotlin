package util

import com.beust.klaxon.*
import game.world.World
import game.world.WorldState
import game.world.toRoomContent
import java.awt.Point
import java.io.FileReader

class JsonParser {
    data class Data(val x: Int, val y: Int, val content: List<String>)

    companion object {
        fun buildFromJsonFile(fileName: String): World {
            val jsonReader = JsonReader(FileReader(fileName))
            val klaxon = Klaxon()
            var world = World(WorldState(0))

            jsonReader.use {
                it.beginObject {
                    while (it.hasNext()) {
                        val readName = it.nextName()
                        when (readName) {
                            "world-size" -> world = World(WorldState(it.nextInt()))
                            "data" -> world.parseDataArray(klaxon, it)
                        }
                    }
                }
            }
            return world
        }

        private fun World.parseDataArray(klaxon: Klaxon, reader: JsonReader) {
            reader.beginArray {
                while (reader.hasNext()) {
                    val data = klaxon.parse<Data>(reader)
                    if (data != null) {
                        for (contentChar in data.content) {
                            val roomContent = contentChar.toRoomContent()
                            this.addRoomContent(Point(data.x, data.y), roomContent)
                        }
                    }
                }
            }
        }
    }
}
