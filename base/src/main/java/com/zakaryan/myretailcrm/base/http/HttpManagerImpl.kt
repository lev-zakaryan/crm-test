package com.zakaryan.myretailcrm.base.http

import android.content.Context
import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import com.zakaryan.myretailcrm.base.http.result.HttpResult
import com.zakaryan.myretailcrm.base.http.retrofit.RetrofitService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class HttpManagerImpl(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HttpManager {

    private val retrofitService = RetrofitService(context)

    override fun getOkHttpClient(): OkHttpClient = retrofitService.okhttpClient

    override suspend fun auth(clientUrl: String, authParams: AuthParams): HttpResult<AuthResponse> {
        return withContext(ioDispatcher) {
            try {
                val response = retrofitService.api.auth(clientUrl, authParams)
                HttpResult.from(response)
            } catch (e: Exception) {
                HttpResult.Exception(e)
            }
        }
    }

    override suspend fun <T : QueryResponse> query(
        clientUrl: String,
        accessToken: String,
        query: QueryRequest<T>
    ): HttpResult<T> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    retrofitService.api.query(clientUrl, "Bearer $accessToken", query.data)
                HttpResult.fromStringResponse(response, query.responseClass)
            } catch (e: Exception) {
                HttpResult.Exception(e)
            }
        }
    }

}