package com.zakaryan.myretailcrm.data.source.remote

import com.zakaryan.myretailcrm.base.http.model.data.AuthParams
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.base.http.model.request.customers.CustomerRequest
import com.zakaryan.myretailcrm.base.http.model.request.customers.CustomersListRequest
import com.zakaryan.myretailcrm.base.http.result.HttpResult
import com.zakaryan.myretailcrm.data.http.HttpManagerProxy
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.data.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSourceImpl
@Inject constructor(
    private val httpManager: HttpManagerProxy,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {

    override suspend fun auth(clientUrl: String, user: String, pass: String): Result<Unit> {
        return withContext(ioDispatcher) {
            when (val result = httpManager.auth(clientUrl, AuthParams.fromPassword(user, pass))) {
                is HttpResult.Success -> {
                    Result.Success(Unit)
                }
                else -> {
                    Result.HttpError(result)
                }
            }
        }
    }

    override suspend fun loadCustomersList(): Result<List<Customer>> {
        return withContext(ioDispatcher) {
            when (val result = httpManager.query(CustomersListRequest())) {
                is HttpResult.Success -> {
                    Result.Success(result.response.customers)
                }
                else -> {
                    Result.HttpError(result)
                }
            }
        }
    }

    override suspend fun loadCustomerDetails(customerId: String): Result<Customer> {
        return withContext(ioDispatcher) {
            when (val result = httpManager.query(CustomerRequest(customerId))) {
                is HttpResult.Success -> {
                    Result.Success(result.response.customer)
                }
                else -> {
                    Result.HttpError(result)
                }
            }
        }
    }
}