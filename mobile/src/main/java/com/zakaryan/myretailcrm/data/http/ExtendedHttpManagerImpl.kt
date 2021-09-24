package com.zakaryan.myretailcrm.data.http

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zakaryan.myretailcrm.base.http.HttpManager
import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult
import com.zakaryan.myretailcrm.oauth.TokenManager
import com.zakaryan.myretailcrm.oauth.model.TokenCache
import com.zakaryan.myretailcrm.ui.livedata.nonNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExtendedHttpManagerImpl @Inject constructor(
    private val httpManager: HttpManager,
    private val tokenManager: TokenManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExtendedHttpManager {

    @Volatile
    private var tokenCache: TokenCache = TokenCache.Unknown

    private val noTokenException = HttpResult.Exception(MissingTokenException())

    private val _authState = MutableLiveData<Boolean>(null)
    override val authState: LiveData<Boolean> = _authState.nonNull()

    override suspend fun loadTokenCache() {
        withContext(ioDispatcher) {
            synchronized(tokenCache) {
                tokenCache = tokenManager.readTokenCache()
                _authState.postValue(tokenCache.isAccess)
            }
        }
    }

    override suspend fun auth(url: String, params: AuthParams): HttpResult<AuthResponse> {
        val result = httpManager.auth(url, params)
        if (result is HttpResult.Success) {
            val response = result.response
            withContext(ioDispatcher) {
                synchronized(tokenCache) {
                    tokenCache = TokenCache.Access(url, response.accessToken, response.refreshToken)
                    tokenManager.saveTokenCache(tokenCache as TokenCache.Access)
                    _authState.postValue(true)
                }
            }
        }
        return result
    }

    override suspend fun clearTokenCache() {
        withContext(ioDispatcher) {
            synchronized(tokenCache) {
                tokenManager.clearTokenCache()
                tokenCache = TokenCache.Empty
                _authState.postValue(false)
            }
        }
    }

    override suspend fun <T : QueryResponse> query(
        query: QueryRequest<T>
    ): HttpResult<T> {
        if (tokenCache is TokenCache.Unknown) {
            // наличие токена не проверялось - проверяем
            loadTokenCache()
        }
        val result = when (tokenCache) {
            is TokenCache.Access -> {
                // есть сохраненный токен - выполняем запрос
                val (url, token) = tokenCache as TokenCache.Access
                httpManager.query(url, token, query)
            }
            is TokenCache.Empty -> {
                // сохраненных токенов нет - сообщаем, что нужна авторизация
                noTokenException
            }
            is TokenCache.Unknown -> {
                // не должно происходить - сообщаем, что нужна авторизация
                noTokenException
            }
        }
        if ((result is HttpResult.HttpError) && (result.httpCode == 401)) {
            // HTTP 401 Unauthorized
            // сервер не принял токен
            // Пытаемся обновить (refresh) токен
            val (url, token, refresh) = tokenCache as TokenCache.Access
            val authResult = auth(url, AuthParams.fromRefreshToken(refresh))
            if (authResult is HttpResult.Success) {
                // у нас должен быть новый токен - повторяем запрос
                return httpManager.query(url, token, query)
            } else {
                // refresh token тоже истек - нужно вводить логин\пароль заново
                clearTokenCache()
                @Suppress("UNCHECKED_CAST")
                return authResult as HttpResult<T>
            }
        } else {
            if (result == noTokenException) {
                clearTokenCache()
            }
            return result
        }
    }

}