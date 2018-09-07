package game.world.effect

import game.world.World
import game.world.condition.GameObjectCondition
import java.awt.Point

class ConditionalEffect(private val worldEffect: WorldEffect, private val condition: GameObjectCondition):
        WorldEffect(worldEffect.gameObject) {
    override fun applyEffect(world: World, point: Point): Boolean {
        if (condition.conditionSatisfied(point, world)) {
            return worldEffect.applyEffect(world, point)
        }
        return false
    }

    override fun removeEffect(world: World, point: Point) {
        worldEffect.removeEffect(world, point)
    }

    override fun roomsAffected(point: Point): Set<Point> {
        return worldEffect.roomsAffected(point)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConditionalEffect

        if (worldEffect != other.worldEffect) return false
        if (condition != other.condition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = worldEffect.hashCode()
        result = 31 * result + condition.hashCode()
        return result
    }

    override fun toString(): String {
        return "ConditionalEffect(worldEffect=$worldEffect, condition=$condition)"
    }
}