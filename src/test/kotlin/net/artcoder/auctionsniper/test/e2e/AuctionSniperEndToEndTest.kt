package net.artcoder.auctionsniper.test.e2e

import org.testng.annotations.Test

class AuctionSniperEndToEndTest {

    private val auction = FakeAuctionServer("item-54321")
    private val application = Application(auction.hostname)

    @Test
    fun sniperJoinsAuctionUntilAuctionCloses() {
        auction.startSellingItem()
        application.startBiddingIn(auction)

        auction.assertReceivedJoinRequestFrom(application.sniperXmppId)

        auction.announceClosed()
        application.assertSniperHasLostAuction()
    }
}

