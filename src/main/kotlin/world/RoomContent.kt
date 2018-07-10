package world

/**
 * Changes:
 *      All AGENT types are removed in favor of a data structure containing the client's position, direction faced,
 *      and inventory which will be used to render the agent appropriately on the map.
 */
enum class RoomContent {
    BLOCKADE,
    BREEZE,
    BUMP,
    FOOD,
    GLITTER,
    GOLD,
    MOO,
    PIT,
    STENCH,
    SUPMUW_EVIL,
    SUPMUW,
    WUMPUS
}