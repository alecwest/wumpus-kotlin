package game.player

data class PlayerInventory(private val inventoryItems: Map<InventoryItem, Int> = mapOf()) {
    fun getInventory() = inventoryItems
    fun copyThis(inventoryItem: Map<InventoryItem, Int> =
                         this.getInventory()) = PlayerInventory(inventoryItem)
}