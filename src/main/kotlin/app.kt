import game.agent.Agent
import game.agent.intelligence.HumanIntelligence
import game.agent.intelligence.KnowledgeBasedIntelligence2
import game.client.Client
import game.server.Server

fun main(args: Array<String>) {
val agent = Agent(Client("src/main/resources/originalMap.json"), KnowledgeBasedIntelligence2())

    while (Server.getGame(0).isPlayerAlive()) {
        agent.makeNextMove()
        println("Enter to continue: ")
        readLine()
    }
}
