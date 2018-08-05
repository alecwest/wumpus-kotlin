import game.agent.Agent
import game.agent.intelligence.HumanIntelligence
import game.client.Client
import game.server.Server

fun main(args: Array<String>) {
    val agent = Agent(Client(), HumanIntelligence())

    while (Server.getGame(0).isPlayerAlive()) {
        agent.makeNextMove()
    }
}
