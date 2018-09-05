package game.world.condition

import facts.Fact
import facts.Fact.*
import game.world.GameObject
import game.world.World
import util.adjacents
import util.diagonals
import java.awt.Point

class ProximityCondition(private val fact: Fact, private val thing: GameObject) : GameObjectCondition(fact, thing) {
    override fun conditionSatisfied(targetLocation: Point, world: World): Boolean {
        return if (fact == HAS) {
            (targetLocation.adjacents() + targetLocation.diagonals()).any {
                world.hasGameObject(targetLocation, thing)
            }
        } else {
            (targetLocation.adjacents() + targetLocation.diagonals()).all {
                !world.hasGameObject(targetLocation, thing)
            }
        }
    }
}