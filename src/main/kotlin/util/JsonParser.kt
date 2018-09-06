package util

import com.beust.klaxon.*
import game.Game
import game.GameState
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import game.player.toInventoryItem
import game.world.World
import game.world.toMappableGameObject
import java.awt.Point
import java.io.File

internal class JsonPlayer(val x: Int, val y: Int, val direction: String, val inventory: List<String>)

internal class JsonRoomContent(val x: Int, val y: Int, val content: List<String>)

internal class JsonWorld(val `world-size`: Int, val data: List<JsonRoomContent>, val player: JsonPlayer)

object JsonParser {
    fun buildFromJsonFile(fileName: String): Game {
        val r = Klaxon().parse<JsonWorld>(File(fileName))
        val world = World(size = r!!.`world-size`)
        val player = Player(PlayerState(
                location = Point(r.player.x, r.player.y),
                facing = r.player.direction.toDirection(),
                inventory = PlayerInventory(mapOf())))
        for (room in r.data) {
            for (gameObjectString in room.content) {
                val gameObject = gameObjectString.toMappableGameObject()
                if (gameObject != null) world.addGameObjectAndEffects(Point(room.x, room.y), gameObject)
            }
        }
        for (inventoryItemString in r.player.inventory) {
            player.addToInventory(inventoryItemString.toInventoryItem())
        }

        return Game(GameState(world = world, player = player))
    }
}
