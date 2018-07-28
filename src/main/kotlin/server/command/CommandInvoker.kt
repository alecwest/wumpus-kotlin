package server.command

object CommandInvoker {
    var command: Command? = null

    fun performAction() {
        command?.execute()
    }
}