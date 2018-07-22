package server.command

class CommandInvoker {
    var command: Command
        get() = command
        set(value) {
            command = value
        }

    fun performAction() {
        command.execute()
    }
}