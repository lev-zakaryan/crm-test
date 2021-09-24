package com.zakaryan.myretailcrm.data.http

import androidx.lifecycle.LiveData

interface AuthManager {

    val authState: LiveData<Boolean>

    suspend fun loadTokenCache()

    suspend fun clearTokenCache()

}