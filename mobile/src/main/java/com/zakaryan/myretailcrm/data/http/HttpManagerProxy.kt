package com.zakaryan.myretailcrm.data.http

import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult

interface HttpManagerProxy {

    suspend fun auth(url: String, params: AuthParams): HttpResult<AuthResponse>

    suspend fun <T : QueryResponse> query(
        query: QueryRequest<T>
    ): HttpResult<T>

}