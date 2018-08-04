import game.Game
import game.GameState
import game.agent.Agent
import game.agent.intelligence.BasicIntelligence
import game.agent.intelligence.Intelligence
import game.client.Client
import game.command.Command
import game.command.CommandResult
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import game.world.Room
import game.world.RoomContent
import game.world.World
import org.junit.jupiter.api.Assertions.assertEquals
import game.server.Server
import game.world.Perception
import util.Direction
import java.awt.Point

class Helpers {
    companion object {
        const val worldFileName = "src/test/resources/testFile.json"
        const val basicIntelligenceWorldFileName = "src/test/resources/basicIntelligenceTestFile.json"

        // TODO all of these create functions shouldn't just be test helpers
        fun createRoom(roomContent: ArrayList<RoomContent>
                       = arrayListOf(RoomContent.BREEZE, RoomContent.STENCH)): Room {
            return Room(roomContent)
        }

        fun createWorld(size: Int = 10,
                        roomContent: Map<Point, ArrayList<out RoomContent>> =
                                mapOf(Point(2, 2) to arrayListOf(RoomContent.PIT))): World {
            val world = World(size)
            for (point in roomContent.keys) {
                for (content in roomContent.getValue(point)) {
                    world.addRoomContent(point, content)
                }
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

        fun createPlayerState(alive: Boolean = true,
                              location: Point = Point(0, 0),
                              facing: Direction = Direction.NORTH,
                              inventoryContent: Map<InventoryItem, Int> =
                                    mapOf(InventoryItem.ARROW to 2)): PlayerState {
            return PlayerState(alive, location, facing, Helpers.createPlayerInventory(inventoryContent))
        }

        fun createPlayerInventory(inventoryContent: Map<InventoryItem, Int> =
                                          mapOf(InventoryItem.ARROW to 2)): PlayerInventory {
            return PlayerInventory(inventoryContent)
        }

        fun createGame(active: Boolean = true,
                       world: World = createWorld(),
                       player: Player = createPlayer()): Game {
            return Game(GameState(active, world, player))
        }

        fun createServerSession(fileName: String = Helpers.worldFileName,
                                worldSize: Int = 10): Int {
            return Server.newSession(fileName = fileName, worldSize = worldSize)
        }

        fun createCommandResult(perceptions: ArrayList<Perception> = arrayListOf(),
                                playerState: PlayerState = Helpers.createPlayerState(),
                                roomContent: ArrayList<RoomContent> = arrayListOf()): CommandResult {
            return CommandResult(perceptions, playerState, roomContent)
        }

        fun createClient(fileName: String = "",
                         worldSize: Int = 10): Client {
            return Client(fileName, worldSize)
        }

        fun createAgent(client: Client = Helpers.createClient(),
                        intelligence: Intelligence = BasicIntelligence()): Agent {
            return Agent(client, intelligence)
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
