package com.zakaryan.myretailcrm.base.http.api

import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.auth.AuthResponse
import retrofit2.Response
import retrofit2.http.*

interface RetailCrmApi {

    @Headers("Content-Type: application/json")
    @POST("https://{url}/oauth/v2/token")
    suspend fun auth(
        @Path("url") url: String,
        @Body params: AuthParams
    ): Response<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("https://{url}/app/api")
    suspend fun query(
        @Path("url") url: String,
        @Header("Authorization") authHeader: String,
        @Body query: QueryRequest.Data
    ): Response<String>

}