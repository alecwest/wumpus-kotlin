package util

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import server.Server
import world.World
import world.toRoomContent
import java.awt.Point
import java.io.FileReader
import java.io.Reader

class JsonParser {
    companion object {
        fun buildFromJsonFile(fileName: String): World {
            return buildFromJson(JsonReader(FileReader(fileName)))
        }

        fun buildFromJson(reader: Reader): World {
            val jsonReader = JsonReader(reader)
            val klaxon = Klaxon()
            var world = World(0)
            jsonReader.use {
                it.beginObject {
                    while (it.hasNext()) {
                        val readName = it.nextName()
                        when (readName) {
                            "world-size" -> world = World(it.nextInt())
                            "data" -> parseDataArray(klaxon, it, world)
                        }
                    }
                }
            }
            return world
        }

        private fun parseDataArray(klaxon: Klaxon, reader: JsonReader, world: World) {
            reader.beginArray {
                while (reader.hasNext()) {
                    val data = klaxon.parse<Server.Data>(reader)
                    if (data != null) {
                        for (contentChar in data.content) {
                            val roomContent = contentChar.toRoomContent()
                            world.addRoomContent(Point(data.x, data.y), roomContent)
                        }
                    }
                }
            }
        }
    }
}
