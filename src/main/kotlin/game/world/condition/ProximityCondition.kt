package game.world.condition

import facts.Fact
import game.world.GameObject
import game.world.World
import java.awt.Point

class ProximityCondition(fact: Fact, thing: GameObject) : GameObjectCondition(fact, thing) {
    override fun conditionSatisfied(targetLocation: Point, world: World): Boolean {
        return true
    }
}