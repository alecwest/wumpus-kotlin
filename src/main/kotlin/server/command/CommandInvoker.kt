package server.command

class CommandInvoker {
    private var command: Command? = null

    fun setCommand(command: Command) {
        this.command = command
    }

    fun performAction() {
        command?.execute()
    }
}