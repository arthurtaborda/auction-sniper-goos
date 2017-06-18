package net.artcoder.auctionsniper.test.e2e

import com.google.common.truth.Truth.assertThat
import net.artcoder.auctionsniper.Main

class Application(val serverHostname: String) {

    val sniperId = "sniper"
    val sniperPassword = "sniper"
    val sniperXmppId = "$sniperId@$serverHostname/Auction"

    val api: ApplicationApi = ApplicationApi()

    fun startBiddingIn(auction: FakeAuctionServer) {
        val thread = Thread({
            try {
                Main.main(arrayOf(serverHostname, sniperId, sniperPassword, auction.itemId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, "main")
        thread.isDaemon = true
        thread.start()
        assertThat(api.getSniperStatus()).isEqualTo(Main.STATUS_JOINING)
    }

    fun assertSniperHasLostAuction() {
        assertThat(api.getSniperStatus()).isEqualTo(Main.STATUS_LOST)
    }

}

