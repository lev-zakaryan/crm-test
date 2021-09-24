package com.zakaryan.myretailcrm.base.http.model.request.customers

import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.customers.CustomersListResponse

class CustomersListRequest :
    QueryRequest<CustomersListResponse>(CustomersListResponse::class.java) {

    override val query: String = "{customers{edges{node{id,firstName}}}}"
}