package util

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import server.Server
import world.World
import world.toRoomContent
import java.awt.Point
import java.io.FileReader

class JsonParser {
    companion object {
        fun buildFromJson(fileName: String): World {
            val klaxon = Klaxon()
            var world = World(0)

            JsonReader(FileReader(fileName)).use { reader ->
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
