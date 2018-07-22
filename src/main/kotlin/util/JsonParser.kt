package util

import com.beust.klaxon.*
import game.Game
import game.GameState
import game.world.World
import game.world.WorldState
import game.world.toRoomContent
import java.awt.Point
import java.io.FileReader

class JsonParser {
    data class Data(val x: Int, val y: Int, val content: List<String>)

    companion object {
        fun buildFromJsonFile(fileName: String): Game {
            val jsonReader = JsonReader(FileReader(fileName))
            val klaxon = Klaxon()
            var world = World(0)

            jsonReader.use {
                it.beginObject {
                    while (it.hasNext()) {
                        val readName = it.nextName()
                        when (readName) {
                            "world-size" -> world = World(it.nextInt())
                            "data" -> world.parseDataArray(klaxon, it)
                        }
                    }
                }
            }
            return Game(GameState(world = world))
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
