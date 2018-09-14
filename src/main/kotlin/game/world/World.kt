package game.world

import game.player.PlayerState
import game.world.effect.*
import game.world.GameObjectFeature.*
import util.adjacents
import util.diagonals
import java.awt.Point

/**
 * Notes:
 * Both the server and the client should be able to utilize one world class
 *
 * When the game starts, the server creates a world and fills in its contents either via a file
 * or procedural generation. The world info will include the following:
 *      Size of the world (one digit, since the world will be square)
 *      List of coordinates for each type of obstacle (no particular order needed), that don't go out of bounds.
 *          Coordinates start at (0,0) at the bottom left, like any graph
 *      Coordinate for player to start at. This point will have no obstacles or content in it.
 *
 * The client, on the other hand, will attempt to establish itself as a player in this new world by contacting the
 * server. Upon success, the client creates a new world for itself with only knowledge of the following:
 *      Size and starting location on map is unknown.
 *          Map fully "exists" but client doesn't know rooms outside of the starting room
 *          For visual effect, rooms that are not yet visited will not be printed. (How?)
 *              Client keeps track of rooms it has visited and what it knows/thinks about each
 *              The list of visited rooms will be passed to a printRooms function
 *      Dimensions of the world are always square
 *      The starting space is guaranteed safe
 *
 * The World class simply acts as an aggregate for all known/accumulated knowledge of each room, whether it's for the
 * server or the client. When the player makes a move, they will declare there move to the server, who then queries its
 * own world instance for what exists in the target room. Upon return of this information, the server will pass it on
 * to the client, who then is able to update their own world instance.
 *
 * Storage of room knowledge should be contained in arraylist of rooms. For the server, this is easy. For the client,
 * there may be a frequent need to update the array based on changes in the known size of the map, thus altering
 * their understanding of coordinates for each room. The client will need to make sure they do not lose or damage what
 * knowledge they already have in doing this.
 *
 * Start:
 *      1. Server reads size number from file and sets size
 *      2. Server creates empty world (initialized to size requested in file)
 *      3. Server reads coordinates of room and the list of RoomContents assigned to that room
 *      4. Server adds each RoomContent to room with given coordinates
 *      5. World updates room and any adjacent rooms that should have related content
 *          A. Each RoomContent has an associated list of WorldEffects (None, AddAdjacent, AddDiagonal, AddOn)
 *          B. Each WorldEffect implements a method applyEffect(x, y, world) that returns a world with the effect
 *          applied appropriately around/on the coordinates
 *          C. Rooms that are not yet visited are still updated. That way when the client considers that room as an option,
 *          they already know about any effects that have been applied to that room
 *      6. When server is ready, it will begin receiving requests from clients for a connection
 *      7. Client starts up and initializes a player and connection request
 *      8. Server responds with a session id, world size, and the starting room information (empty room)
 *          A. The agent should not have any information about the coordinates of the point
 *              I.  Let the agent use an empty world and assume its starting point is the bottom left.
 *                  As it travels, it will attempt to go beyond the map boundaries, and if successful, all rooms are
 *                  shifted in the opposite direction.
 *                  This will require also updating their knowledge on what content exists at which coordinates
 *              II. Let the agent create a graph of the world.
 *                  I'm less familiar with these, but it may be faster / more efficient since shifting won't be needed.
 *      9. Client sends movement request -> move(sessionId, direction)
 *      10. Server sends success -> success(sessionId, roomContent, playerState); or failure ->
 *          fail(sessionId, roomContent, playerState, reason(probably also room content))
 *      11. Client responds to success or failure by updating room positions, knowledge, and whether or not it's alive
 */
data class World(private var size: Int = 10) {
    private var worldState: WorldState = WorldState()

    init {
        val worldRooms = arrayListOf<Room>()
        for (i in 0..(size * size - 1)) {
            worldRooms.add(Room())
        }
        worldState = worldState.copyThis(rooms = worldRooms)
    }

    fun getSize() = size
    fun getRooms() = worldState.getRooms()

    fun getGameObjects(point: Point) = worldState.getGameObjects(point)

    fun addGameObjectAndEffects(point: Point, content: GameObject) {
        if (!roomIsValid(point)) return
        addGameObject(point, content)
        val worldAffectingFeature = (content.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting?)
        if (worldAffectingFeature != null) addWorldEffects(point, worldAffectingFeature.effects)

        var numberEffectsApplied = 1
        while (numberEffectsApplied > 0) {
            numberEffectsApplied = 0
            (point.adjacents() + point.diagonals()).forEach { neighbor ->
                /**
                 * Each neighbor checks through content in their room.
                 * If that content has a feature that is conditional on the proximity of the object being added,
                 * add or remove the feature
                 */
                getGameObjects(neighbor)
                        .filter { it.hasFeature(WorldAffecting()) }
                        .map { it.getFeature(WorldAffecting()) as WorldAffecting }
                        .forEach { neighborGameObjectConditionalEffect ->
                            if (neighborGameObjectConditionalEffect.createsEffect(neighbor, this)) {
                                if (!neighborGameObjectConditionalEffect.effects.all { hasGameObject(neighbor, it.gameObject) }) {
                                    numberEffectsApplied +=
                                            addWorldEffects(neighbor, neighborGameObjectConditionalEffect.effects)
                                }
                            } else {
                                numberEffectsApplied +=
                                        removeWorldEffects(neighbor, neighborGameObjectConditionalEffect.effects)
                            }
                        }
            }

        }
    }

    // Add a game object with no consideration for its world effects
    fun addGameObject(point: Point, gameObject: GameObject) {
        worldState = worldState.copyThis(rooms = worldState.getRooms().apply {
            this[getRoomIndex(point)].addGameObject(gameObject)
        })
    }

    internal fun addWorldEffects(point: Point, worldEffects: ArrayList<WorldEffect>): Int {
        return worldEffects.count { worldEffect ->
            worldEffect.applyEffect(this, point)
        }
    }

    fun removeGameObject(point: Point, content: GameObject) {
        worldState = worldState.copyThis(rooms = worldState.getRooms().apply {
            try {
                this[getRoomIndex(point)].removeGameObject(content)
                val worldAffectingFeature = (content.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting?)
                if (worldAffectingFeature != null) removeWorldEffects(point, worldAffectingFeature.effects)
            } catch (e: ArrayIndexOutOfBoundsException) { }
        })
    }

    internal fun removeWorldEffects(point: Point, worldEffects: ArrayList<WorldEffect>): Int {
        return worldEffects.count { worldEffect ->
            worldEffect.removeEffect(this, point)
        }
    }

    fun hasGameObject(point: Point, content: GameObject) = worldState.hasGameObject(point, content)
    fun roomIsValid(point: Point) = worldState.roomIsValid(point)
    fun roomIsEmpty(point: Point) = worldState.roomIsEmpty(point)
    fun getRoomIndex(point: Point) = worldState.getRoomIndex(point)
    fun getRoomPoint(index: Int) = worldState.getRoomPoint(index)
    fun getWorldMap(playerState: PlayerState? = null) = worldState.getWorldMap(playerState)
    fun getRoom(point: Point) = worldState.getRoom(point)
    fun getNumberRooms() = worldState.getNumberRooms()
    fun getAmountOfObjectsInRoom(point: Point) = worldState.getAmountOfObjectsInRoom(point)
}
