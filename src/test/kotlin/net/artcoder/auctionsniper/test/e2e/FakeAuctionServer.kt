package net.artcoder.auctionsniper.test.e2e

import com.google.common.truth.Truth.assertThat
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

class FakeAuctionServer(val itemId: String) {

    var currentChat: Chat? = null
    val messages: ArrayBlockingQueue<Message> = ArrayBlockingQueue(1)

    val hostname = "localhost"
    val password = "auction"
    val resource = "Auction"
    val connection = XMPPConnection(hostname)

    fun startSellingItem() {
        connection.connect()
        val username = "auction-$itemId"
        connection.login(username, password, resource)
        println("Connected user: $username, pass: $password, resource: $resource")
        connection.chatManager.addChatListener({ chat, createdLocally ->
            currentChat = chat
            println("Opened chat")
            chat.addMessageListener({ chat, message ->
                println("Message Received")
                messages.add(message)
            })
        })
    }

    fun assertReceivedJoinRequestFrom(sniperXmppId: String) {
        assertThat(messages.poll(3, TimeUnit.SECONDS)).isNotNull()
    }

    fun announceClosed() {
        currentChat?.sendMessage(Message())
    }

}
