package com.zakaryan.myretailcrm.base.http

import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult
import okhttp3.OkHttpClient

interface HttpManager {

    fun getOkHttpClient(): OkHttpClient

    suspend fun auth(clientUrl: String, authParams: AuthParams): HttpResult<AuthResponse>

    suspend fun <T : QueryResponse> query(
        clientUrl: String,
        accessToken: String,
        query: QueryRequest<T>
    ): HttpResult<T>

}