package game.player

/**
 * InventoryItem is a collection of items that can exist in a player's inventory
 */
enum class InventoryItem {
    ARROW,
    FOOD,
    GOLD

}

/**
 * @return [String] representation of inventory item
 */
fun InventoryItem.toCharRepresentation() = when(this) {
    InventoryItem.ARROW -> "A"
    InventoryItem.FOOD -> "F"
    InventoryItem.GOLD -> "G"
}

/**
 * @return [InventoryItem] representation of String
 */
fun String.toInventoryItem() = when(this) {
    "A" -> InventoryItem.ARROW
    "F" -> InventoryItem.FOOD
    "G" -> InventoryItem.GOLD
    else -> throw Exception("Cannot convert %s to an InventoryItem value".format(this))
}

