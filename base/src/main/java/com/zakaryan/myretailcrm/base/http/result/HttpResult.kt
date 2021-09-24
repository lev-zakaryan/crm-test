package com.zakaryan.myretailcrm.base.http.result

import androidx.annotation.WorkerThread
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.zakaryan.myretailcrm.base.http.model.response.BaseResponse
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse
import com.zakaryan.myretailcrm.base.json.JsonMapper
import retrofit2.Response

sealed class HttpResult<out T : BaseResponse> {

    data class Success<out T : BaseResponse>(val response: T) : HttpResult<T>()
    data class ApiError(val error: String) : HttpResult<Nothing>()
    data class HttpError(val httpCode: Int) : HttpResult<Nothing>()
    data class Exception(val throwable: Throwable) : HttpResult<Nothing>()

    val isSuccessful: Boolean get() = this is Success

    companion object {

        fun <T : BaseResponse> from(response: Response<T>): HttpResult<T> {
            return if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    if (responseBody.isSuccessful) {
                        Success(responseBody)
                    } else {
                        ApiError(responseBody.error)
                    }
                } else {
                    Exception(IllegalStateException("Retrofit delivered null response."))
                }
            } else {
                HttpError(response.code())
            }
        }

        @WorkerThread
        @Throws(JsonProcessingException::class, JsonMappingException::class)
        fun <T : QueryResponse> fromStringResponse(
            response: Response<String>,
            responseClass: Class<T>
        ): HttpResult<T> {
            return if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val queryResponse =
                        JsonMapper.instance.readValue(response.body(), responseClass)
                    if (queryResponse.isSuccessful) {
                        Success(queryResponse)
                    } else {
                        ApiError(queryResponse.error)
                    }
                } else {
                    Exception(IllegalStateException("Retrofit delivered null response."))
                }
            } else {
                HttpError(response.code())
            }
        }
    }

}