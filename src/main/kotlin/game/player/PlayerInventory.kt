package game.player

data class PlayerInventory(private val inventoryItems: Map<InventoryItem, Int> = mapOf()) {
    fun getInventory() = inventoryItems
    fun copyThis(inventoryItem: Map<InventoryItem, Int> =
                         this.getInventory()) = PlayerInventory(inventoryItem)

    override fun toString(): String {
        var result = ""
        for (inventoryItem in inventoryItems.keys) {
            result += "\n\t$inventoryItem -> ${inventoryItems[inventoryItem]}"
        }
        return result
    }

}