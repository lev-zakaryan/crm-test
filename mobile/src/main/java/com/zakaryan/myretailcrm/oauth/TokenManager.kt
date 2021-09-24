package com.zakaryan.myretailcrm.oauth

import com.zakaryan.myretailcrm.oauth.model.TokenCache

interface TokenManager {

    fun saveTokenCache(cache: TokenCache.Access)

    fun readTokenCache(): TokenCache

    fun clearTokenCache()

}