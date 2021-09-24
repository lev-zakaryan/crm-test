package com.zakaryan.myretailcrm.oauth.model

sealed class TokenCache {

    data class Access(
        val url: String,
        val accessToken: String,
        val refreshToken: String
    ) : TokenCache()

    object Empty : TokenCache()

    object Unknown : TokenCache()

    val isEmpty: Boolean get() = this is Empty

    val isAccess: Boolean get() = this is Access

}