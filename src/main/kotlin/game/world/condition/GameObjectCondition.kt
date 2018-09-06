package game.world.condition

import facts.Condition
import facts.Fact
import game.world.GameObject
import game.world.World
import java.awt.Point

abstract class GameObjectCondition(fact: Fact, thing: GameObject) : Condition<GameObject>(fact, thing) {
    abstract fun conditionSatisfied(targetLocation: Point, world: World): Boolean
}