package com.zakaryan.myretailcrm.base.http.model.response.customers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.base.http.model.deserializer.CustomerDeserializer
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerResponse(

    @JsonProperty("data")
    @JsonDeserialize(using = CustomerDeserializer::class)
    val customer: Customer = Customer()

) : QueryResponse()