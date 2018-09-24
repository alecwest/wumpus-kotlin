package game.command

/**
 * CommandInvoker is a singleton for executing Commands
 */
object CommandInvoker {
    var command: Command? = null

    /**
     * Execute the assigned command
     */
    fun performAction() {
        command?.execute()
    }
}