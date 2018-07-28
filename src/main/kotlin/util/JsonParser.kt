package util

import com.beust.klaxon.*
import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerState
import game.player.toInventoryItem
import game.world.World
import game.world.WorldState
import game.world.toRoomContent
import java.awt.Point
import java.io.File
import java.io.FileReader

internal class JsonPlayer(val x: Int, val y: Int, val direction: String, val inventory: List<String>)

internal class JsonRoomContent(val x: Int, val y: Int, val content: List<String>)

internal class JsonWorld(val `world-size`: Int, val data: List<JsonRoomContent>, val player: JsonPlayer)

class JsonParser {
    companion object {
        fun buildFromJsonFile(fileName: String): Game {
            val r = Klaxon().parse<JsonWorld>(File(fileName))
            val world = World(size = r!!.`world-size`)
            val player = Player(PlayerState(location = Point(r.player.x, r.player.y),
                    facing = r.player.direction.toDirection()))
            for (room in r.data) {
                for (roomContentString in room.content) {
                    world.addRoomContent(Point(room.x, room.y), roomContentString.toRoomContent())
                }
            }
            for (inventoryItemString in r.player.inventory) {
                player.addToInventory(inventoryItemString.toInventoryItem())
            }

            return Game(GameState(world = world, player = player))
        }
    }
}
