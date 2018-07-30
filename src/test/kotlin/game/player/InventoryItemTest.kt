package game.player

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class InventoryItemTest {
    @Test
    fun `convert between string and inventory item`() {
        for (inventoryItem in InventoryItem.values()) {
            assertEquals(inventoryItem, inventoryItem.toCharRepresentation().toInventoryItem())
        }
    }

    @Test
    fun `convert invalid string`() {
        assertFails { "invalid string".toInventoryItem() }
    }
}