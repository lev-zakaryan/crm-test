package com.zakaryan.myretailcrm.base.http.model.response.customers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.base.http.model.deserializer.CustomerListDeserializer
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomersListResponse(

    @JsonProperty("data")
    @JsonDeserialize(using = CustomerListDeserializer::class)
    val customers: List<Customer> = emptyList()

) : QueryResponse()