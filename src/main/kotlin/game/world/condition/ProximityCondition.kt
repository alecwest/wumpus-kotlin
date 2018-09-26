package game.world.condition

import facts.Fact
import facts.Fact.*
import game.world.GameObject
import game.world.World
import util.adjacents
import util.diagonals
import java.awt.Point

/**
 * ProximityCondition is a type of condition that depends on whether or not the given [GameObject] exists
 * in a [Room] surrounding it
 */
class ProximityCondition(fact: Fact, thing: GameObject) : GameObjectCondition(fact, thing) {
    override fun conditionSatisfied(world: World, targetLocation: Point): Boolean {
        return if (fact == HAS) {
            (targetLocation.adjacents() + targetLocation.diagonals()).any {
                world.hasGameObject(it, thing)
            }
        } else {
            (targetLocation.adjacents() + targetLocation.diagonals()).all {
                !world.hasGameObject(it, thing)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProximityCondition

        if (fact != other.fact) return false
        if (thing != other.thing) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fact.hashCode()
        result = 31 * result + thing.hashCode()
        return result
    }

    override fun toString(): String {
        return "ProximityCondition(fact=$fact, thing=$thing)"
    }
}