fun getGreeting(): String {
    val words = mutableListOf<String>()
    words.add("Hello,")
    words.add("game.world!")

    return words.joinToString(separator = " ")
}

fun main(args: Array<String>) {
    println(getGreeting())
}
