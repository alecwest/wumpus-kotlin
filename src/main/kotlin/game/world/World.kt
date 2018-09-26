package game.world

import game.player.PlayerState
import game.world.effect.*
import game.world.GameObjectFeature.*
import util.adjacents
import util.diagonals
import java.awt.Point

/**
 * Represents the entire playing field for all objects and the player
 *
 * @param size fixed size of the world, number of rooms will be (size * size)
 */
data class World(private val size: Int = 10) {
    private var worldState: WorldState = WorldState()

    init {
        val worldRooms = arrayListOf<Room>()
        for (i in 0..(size * size - 1)) {
            worldRooms.add(Room())
        }
        worldState = worldState.copyThis(rooms = worldRooms)
    }

    /**
     * @return [Int] dimensions of the world
     */
    fun getSize() = size

    /**
     * @return [List] all rooms in the world
     */
    fun getRooms() = worldState.getRooms()

    /**
     * @param point room to get objects from
     *
     * @return [Set] of all game objects in the room
     */
    fun getGameObjects(point: Point) = worldState.getGameObjects(point)

    /**
     * Add the given object and any effects it creates
     *
     * @param point location to add the object
     * @param content object to add
     */
    fun addGameObjectAndEffects(point: Point, content: GameObject) {
        if (!roomIsValid(point)) return
        addGameObject(point, content)
        val worldAffectingFeature = (content.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting?)
        if (worldAffectingFeature != null) addWorldEffects(point, worldAffectingFeature.effects)

        var numberEffectsApplied = 1
        while (numberEffectsApplied > 0) {
            numberEffectsApplied = 0
            (point.adjacents() + point.diagonals()).forEach { neighbor ->
                getGameObjects(neighbor)
                        .filter { it.hasFeature(WorldAffecting()) }
                        .map { it.getFeature(WorldAffecting()) as WorldAffecting }
                        .filter { it.hasEffectClass(ConditionalEffect::class) }
                        .forEach { neighborObjectWithConditionalEffect ->
                            val conditionals = neighborObjectWithConditionalEffect.effects
                                    .filter { it is ConditionalEffect } as ArrayList
                            numberEffectsApplied += addWorldEffects(neighbor, conditionals)
                        }
            }

        }
    }

    /**
     * Add a game object with no consideration for its world effects
     *
     * @param point location to add the object
     * @param gameObject object to add
     */
    fun addGameObject(point: Point, gameObject: GameObject) {
        worldState = worldState.copyThis(rooms = worldState.getRooms().apply {
            this[getRoomIndex(point)].addGameObject(gameObject)
        })
    }

    /**
     * Add given list of world effects
     *
     * @param point base location to add the effects from
     * @param worldEffects list of effects to add
     */
    internal fun addWorldEffects(point: Point, worldEffects: ArrayList<WorldEffect>): Int {
        return worldEffects.count { worldEffect ->
            worldEffect.applyEffect(this, point)
        }
    }

    /**
     * Remove given object and its effects (where applicable). Effects will remain if other nearby objects also emit
     * the same object as an effect
     *
     * @param point location to remove the object
     * @param content object to remove
     */
    fun removeGameObject(point: Point, content: GameObject) {
        worldState = worldState.copyThis(rooms = worldState.getRooms().apply {
            try {
                this[getRoomIndex(point)].removeGameObject(content)
                val worldAffectingFeature = (content.getFeature(GameObjectFeature.WorldAffecting()) as GameObjectFeature.WorldAffecting?)
                if (worldAffectingFeature != null) removeWorldEffects(point, worldAffectingFeature.effects)
            } catch (e: ArrayIndexOutOfBoundsException) { }
        })
    }

    /**
     * Remove given list of world effects.
     *
     * @param point base location to remove effects from
     * @param worldEffects list of effects to remove
     *
     * @return [Int] number of effects successfully removed
     */
    internal fun removeWorldEffects(point: Point, worldEffects: ArrayList<WorldEffect>): Int {
        return worldEffects.count { worldEffect ->
            worldEffect.removeEffect(this, point)
        }
    }

    /**
     * @param point room to check
     * @param content object to check for
     *
     * @return [Boolean] indicating room has object
     */
    fun hasGameObject(point: Point, content: GameObject) = worldState.hasGameObject(point, content)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating room is within world boundaries
     */
    fun roomIsValid(point: Point) = worldState.roomIsValid(point)

    /**
     * @param point room to check
     *
     * @return [Boolean] indicating no objects in room
     */
    fun roomIsEmpty(point: Point) = worldState.roomIsEmpty(point)

    /**
     * @param point room to check
     *
     * @return [Int] indicating array index of room
     */
    fun getRoomIndex(point: Point) = worldState.getRoomIndex(point)

    /**
     * @param index array index of room
     *
     * @return [Point] location of room
     */
    fun getRoomPoint(index: Int) = worldState.getRoomPoint(index)

    /**
     * @param playerState optional state of the player
     *
     * @return [String] 2-Dimensional map of the world
     */
    fun getWorldMap(playerState: PlayerState? = null) = worldState.getWorldMap(playerState)

    /**
     * @param point room to get
     *
     * @return [Room]
     */
    fun getRoom(point: Point) = worldState.getRoom(point)

    /**
     * @return [Int] total number of rooms in world
     */
    fun getNumberRooms() = worldState.getNumberRooms()

    /**
     * @param point room to check
     *
     * @return [Int] number of objects in the room
     */
    fun getAmountOfObjectsInRoom(point: Point) = worldState.getAmountOfObjectsInRoom(point)
}
