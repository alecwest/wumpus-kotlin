package server

import com.beust.klaxon.Json
import java.awt.Point

data class WorldInit(
        @Json(name = "world-size")
        val size: Int,
        @Json(name = "data")
        val content: List<Pair<Point, String>>) {
}



