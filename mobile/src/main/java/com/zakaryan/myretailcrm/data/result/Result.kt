package com.zakaryan.myretailcrm.data.result

import com.zakaryan.myretailcrm.base.http.result.HttpResult

sealed class Result<out T: Any> {

    data class Success<out T: Any>(val value: T) : Result<T>()
    data class HttpError(val httpResult: HttpResult<*>) : Result<Nothing>()
    data class DatabaseError(val throwable: Throwable) : Result<Nothing>()

    val isSuccessful: Boolean get() = this is Success
}