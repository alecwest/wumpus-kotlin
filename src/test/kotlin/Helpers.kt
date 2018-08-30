import game.Game
import game.GameState
import game.agent.Agent
import game.agent.intelligence.BasicIntelligence
import game.agent.intelligence.Intelligence
import game.client.Client
import game.command.CommandResult
import game.player.InventoryItem
import game.player.Player
import game.player.PlayerInventory
import game.player.PlayerState
import org.junit.jupiter.api.Assertions.assertEquals
import game.server.Server
import game.world.*
import util.Direction
import java.awt.Point

class Helpers {
    companion object {
        const val testFilePath = "src/test/resources/"

        fun createRoom(gameObject: MutableSet<GameObject>
                       = mutableSetOf(GameObject.BREEZE, GameObject.STENCH)): Room {
            return Room(gameObject)
        }

        fun createWorld(size: Int = 10,
                        gameObject: Map<Point, Set<out GameObject>> =
                                mapOf(Point(2, 2) to setOf(GameObject.PIT))): World {
            val world = World(size)
            for (point in gameObject.keys) {
                for (content in gameObject.getValue(point)) {
                    world.addGameObject(point, content)
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
                       player: Player = createPlayer(),
                       score: Int = 0): Game {
            return Game(GameState(active, world, player, score))
        }

        fun createServerSession(fileName: String = Helpers.testFilePath + "testFile.json",
                                worldSize: Int = 10): Int {
            return Server.newSession(fileName = fileName, worldSize = worldSize)
        }

        fun createCommandResult(perceptions: Set<Perception> = setOf(),
                                playerState: PlayerState = Helpers.createPlayerState()): CommandResult {
            return CommandResult(perceptions, playerState)
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
