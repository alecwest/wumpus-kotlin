package facts

abstract class Condition<T>(private val fact: Fact, private val thing: T) {
    abstract fun conditionSatisfied(): Boolean
}