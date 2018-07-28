package game.player

enum class InventoryItem {
    ARROW,
    FOOD,
    GOLD

}

fun InventoryItem.toCharRepresentation() = when(this) {
    InventoryItem.ARROW -> "A"
    InventoryItem.FOOD -> "F"
    InventoryItem.GOLD -> "G"
}

fun String.toInventoryItem() = when(this) {
    "A" -> InventoryItem.ARROW
    "F" -> InventoryItem.FOOD
    "G" -> InventoryItem.GOLD
    else -> throw Exception("Cannot convert %s to an InventoryItem value".format(this))
}

