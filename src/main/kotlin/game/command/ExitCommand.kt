package game.command

class ExitCommand: Command() {
    override fun execute() {
        game?.let { game ->
            game.setPlayerScore(game.getScore() + getMoveCost(game.getPlayerState()))
            game.setCommandResult(createCommandResult())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExitCommand) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}