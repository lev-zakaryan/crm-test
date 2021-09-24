package com.zakaryan.myretailcrm.data

import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.data.source.LocalDataSource
import com.zakaryan.myretailcrm.data.source.RemoteDataSource
import javax.inject.Inject

class DefaultRepository
@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun auth(clientUrl: String, user: String, pass: String): Result<Unit> {
        return remoteDataSource.auth(clientUrl, user, pass)
    }

    override suspend fun loadCustomersList(): Result<List<Customer>> {
        return remoteDataSource.loadCustomersList()
    }

    override suspend fun loadCustomerDetails(customerId: String): Result<Customer> {
        return remoteDataSource.loadCustomerDetails(customerId)
    }
}