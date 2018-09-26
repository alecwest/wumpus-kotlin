package game.world.condition

import facts.Condition
import facts.Fact
import game.world.GameObject
import game.world.World
import java.awt.Point

/**
 * GameObjectCondition is an abstract implementation of Condition that specifies GameObjects as the thing to
 * apply the condition to
 */
abstract class GameObjectCondition(fact: Fact, thing: GameObject) : Condition<GameObject>(fact, thing) {
    /**
     * Check if this condition is true
     *
     * @param world world to check in
     * @param targetLocation point where the condition should be analyzed at
     */
    abstract fun conditionSatisfied(world: World, targetLocation: Point): Boolean
}