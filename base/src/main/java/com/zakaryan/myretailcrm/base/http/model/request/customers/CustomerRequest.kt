package com.zakaryan.myretailcrm.base.http.model.request.customers

import com.zakaryan.myretailcrm.base.http.model.request.QueryRequest
import com.zakaryan.myretailcrm.base.http.model.response.customers.CustomerResponse

class CustomerRequest(customerId: String) :
    QueryRequest<CustomerResponse>(CustomerResponse::class.java) {

    override val query: String = "{customer(id:$customerId){id,firstName,lastName,patronymic}}"
}