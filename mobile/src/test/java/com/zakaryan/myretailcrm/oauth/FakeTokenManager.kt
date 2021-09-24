package com.zakaryan.myretailcrm.oauth

import com.zakaryan.myretailcrm.oauth.model.TokenCache

class FakeTokenManager : TokenManager {

    private var cache: TokenCache = TokenCache.Empty

    override fun saveTokenCache(cache: TokenCache.Access) {
        this.cache = cache
    }

    override fun readTokenCache(): TokenCache {
        return cache
    }

    override fun clearTokenCache() {
        cache = TokenCache.Empty
    }
    
}