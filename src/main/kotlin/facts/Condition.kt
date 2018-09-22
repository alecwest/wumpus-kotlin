package facts

/**
 * Condition associates a [Fact][facts.Fact] with any one thing
 */
abstract class Condition<T>(internal val fact: Fact, internal val thing: T)