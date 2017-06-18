package net.artcoder.auctionsniper

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message

object Main {

    var chat: Chat? = null // this prevents it to be collected by the garbage collector
    val STATUS_JOINING = "JOINING"
    val STATUS_LOST = "LOST"

    var sniperStatus = STATUS_JOINING


    fun main(args: Array<String>) {
        startHttpServer()
        joinAuction(args[0], args[1], args[2], args[3])
    }

    private fun joinAuction(hostname: String, username: String, password: String, itemId: String) {
        val xmpp = XMPPConnection(hostname)
        xmpp.connect()
        val resource = "Auction"
        xmpp.login(username, password, resource)
        println("Connected user: $username, pass: $password, resource: $resource")
        val auctionUser = "auction-$itemId@$hostname/$resource"
        chat = xmpp.chatManager.createChat(auctionUser,
                { chat: Chat?, message: Message? ->
                    sniperStatus = STATUS_LOST
                })
        chat?.sendMessage(Message())
        println("Message sent to $auctionUser")
    }

    private fun startHttpServer() {
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()

        val router = Router.router(vertx)

        server.requestHandler({ router.accept(it) }).listen(8080)

        router.get("/sniper/status").handler({ ctx ->
            val response = ctx.response()
            response.isChunked = true
            response.write(sniperStatus)
            response.end()
        })
    }
}