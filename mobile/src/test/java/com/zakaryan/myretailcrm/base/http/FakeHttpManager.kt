package com.zakaryan.myretailcrm.base.http

import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult

class FakeHttpManager : HttpManager {

    lateinit var authResult: HttpResult<AuthResponse>

    var queryResult: List<HttpResult<QueryResponse>> = emptyList()
        set(value) {
            field = value
            queryResultIdx = 0
        }
    private var queryResultIdx = 0

    override fun getOkHttpClient(): Nothing {
        throw RuntimeException("Method is not required for testing!")
    }

    override suspend fun auth(clientUrl: String, authParams: AuthParams): HttpResult<AuthResponse> {
        return authResult
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : QueryResponse> query(
        clientUrl: String,
        accessToken: String,
        query: QueryRequest<T>
    ): HttpResult<T> {
        return queryResult[queryResultIdx++] as HttpResult<T>
    }

}