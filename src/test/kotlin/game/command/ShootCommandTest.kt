package game.command

import game.Game
import game.player.InventoryItem
import game.player.PlayerInventory
import game.world.GameObject
import game.world.Perception
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import util.Direction
import java.awt.Point
import java.util.stream.Stream

internal class ShootCommandTest {
    @Test
    fun `shoot commands are equal`() {
        val command = ShootCommand()
        assertEquals(command, command)
        assertNotEquals(command, Direction.EAST)
        assertEquals(command, ShootCommand())
    }

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
        private val initialWorld = Helpers.createWorld(gameObject = mapOf(
                Point(0, 9) to setOf(GameObject.SUPMUW),
                Point(0, 8) to setOf(GameObject.WUMPUS),
                Point(0, 3) to setOf(GameObject.SUPMUW)))
        private val initialGame = Helpers.createGame(player = initialPlayer, world = initialWorld)

        @JvmStatic
        fun validShootCommandTestDataProvider() = Stream.of(
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(setOf(Perception.SCREAM, Perception.EXIT),
                                initialGame.getPlayerState().copyThis(
                                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 3)), score = 1))),
                 ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(setOf(Perception.SCREAM, Perception.EXIT),
                                initialGame.getPlayerState().copyThis(
                                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 2)), score = 2))),
                 ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(setOf(Perception.SCREAM, Perception.EXIT),
                                initialGame.getPlayerState().copyThis(
                                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 1)), score = 3))),
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(setOf(Perception.EXIT),
                                initialGame.getPlayerState().copyThis(
                                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 0)), score = 4))),
                ValidShootCommandTestData(initialGame, ShootCommand(),
                        CommandResult(setOf(Perception.EXIT),
                                initialGame.getPlayerState().copyThis(
                                        inventory = PlayerInventory(mapOf(InventoryItem.ARROW to 0)), score = 5)))
        )
    }
}

// TODO this is the same data class structure in all Command tests
data class ValidShootCommandTestData (
        val givenGame: Game,
        val command: Command,
        val expectedCommandResult: CommandResult
)