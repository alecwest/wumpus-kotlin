package server.command

class MoveInvoker {
    private var command: Command? = null

    fun setCommand(command: Command) {
        this.command = command
    }

    fun makeMove() {
        command?.execute()
    }
}