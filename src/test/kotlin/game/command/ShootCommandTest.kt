package game.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.Perception
import game.world.RoomContent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import java.util.stream.Stream

internal class ShootCommandTest {
    @ParameterizedTest
    @MethodSource("validShootCommandTestDataProvider")
    fun `execute shoot command`(testData: ValidShootCommandTestData) {
        testData.command.setGame(testData.givenGame)
        testData.command.execute()
        assertEquals(testData.expectedCommandResult, testData.givenGame.getCommandResult())
    }

    companion object {
        private val initialPlayer = Helpers.createPlayer(inventoryContent = mapOf(
                InventoryItem.ARROW to 4
        ))
        private val initialWorld = Helpers.createWorld(roomContent = mapOf(
                Point(0, 9) to arrayListOf(RoomContent.SUPMUW_EVIL),
                Point(0, 8) to arrayListOf(RoomContent.WUMPUS),
                Point(0, 3) to arrayListOf(RoomContent.SUPMUW)))
        private val initialGame = Helpers.createGame(player = initialPlayer, world = initialWorld)

        @JvmStatic
        fun validShootCommandTestDataProvider() = Stream.of(
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(arrayListOf(Perception.SCREAM),
                                initialGame.getPlayerState().copyThis(inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 3))), initialGame.getRoomContent())),
                 ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(arrayListOf(Perception.SCREAM),
                                initialGame.getPlayerState().copyThis(inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 2))), initialGame.getRoomContent())),
                 ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(arrayListOf(Perception.SCREAM),
                                initialGame.getPlayerState().copyThis(inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 1))), initialGame.getRoomContent())),
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(arrayListOf(),
                                initialGame.getPlayerState().copyThis(inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 0))), initialGame.getRoomContent())),
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(arrayListOf(),
                                initialGame.getPlayerState().copyThis(inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 0))), initialGame.getRoomContent()))
        )
    }
}

// TODO this is the same data class structure in all Command tests
data class ValidShootCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedCommandResult: CommandResult
)