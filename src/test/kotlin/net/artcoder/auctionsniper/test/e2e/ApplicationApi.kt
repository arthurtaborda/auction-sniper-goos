package net.artcoder.auctionsniper.test.e2e

import io.restassured.RestAssured.get

class ApplicationApi {

    fun getSniperStatus(): String? {
        return get("/sniper/status").asString()
    }
}