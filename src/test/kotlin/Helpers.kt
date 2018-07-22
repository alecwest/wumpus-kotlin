import game.Game
import game.GameState
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import game.world.Room
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.api.Assertions.assertEquals
import util.Direction
import java.awt.Point

class Helpers {
    companion object {
        // TODO all of these create functions shouldn't just be test helpers
        fun createRoom(roomContent: ArrayList<RoomContent>
                       = arrayListOf(RoomContent.BREEZE, RoomContent.STENCH)): Room {
            return Room(roomContent)
        }

        fun createWorld(size: Int = 10,
                        roomContent: Map<Point, RoomContent> =
                                mapOf(Point(2, 2) to RoomContent.PIT)): World {
            val world = World(size)
            for (point in roomContent.keys) {
                world.addRoomContent(point, roomContent.getValue(point))
            }
            return world
        }

        fun createPlayer(alive: Boolean = true,
                         location: Point = Point(0, 0),
                         facing: Direction = Direction.NORTH,
                         inventoryContent: Map<InventoryItem, Int> =
                                 mapOf(InventoryItem.ARROW to 2)): Player {
            return Player(PlayerState(alive, location, facing, PlayerInventory(inventoryContent)))
        }

        fun createGame(active: Boolean = true,
                       world: World = createWorld(),
                       player: Player = createPlayer()): Game {
            return Game(GameState(active, world, player))
        }

        fun assertContains(content: String, subString: String, numExpected: Int) {
            val numFound = content.sumBy {
                if (subString.contains(it))
                    1
                else
                    0
            }
            assertEquals(numExpected, numFound)
        }
    }
}
