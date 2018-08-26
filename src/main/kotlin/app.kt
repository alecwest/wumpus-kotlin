import game.agent.Agent
import game.agent.intelligence.KnowledgeBasedIntelligence
import game.client.Client
import game.server.Server

fun main(args: Array<String>) {
    val agent = Agent(Client("src/main/resources/originalMap.json"), KnowledgeBasedIntelligence())

    while (Server.getGame(0).isPlayerAlive()) {
        agent.makeNextMoves()
//        println("Enter to continue: ")
//        readLine()
    }
}
