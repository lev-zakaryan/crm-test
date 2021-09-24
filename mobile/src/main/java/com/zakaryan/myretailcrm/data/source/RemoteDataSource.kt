package com.zakaryan.myretailcrm.data.source

import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.data.result.Result

interface RemoteDataSource {

    suspend fun auth(clientUrl: String, user: String, pass: String): Result<Unit>

    suspend fun loadCustomersList(): Result<List<Customer>>

    suspend fun loadCustomerDetails(customerId: String): Result<Customer>

}