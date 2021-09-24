package com.zakaryan.myretailcrm.base.http.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.zakaryan.myretailcrm.base.http.model.response.QueryResponse

abstract class QueryRequest<T : QueryResponse>(val responseClass: Class<T>) {

    abstract val query: String

    /* -------------------------- HTTP POST DATA ------------------------------------------------ */

    val data get() = Data.of(query)

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Data private constructor(@JsonProperty("query") val query: String) {
        companion object {
            fun of(query: String) = Data(query)
        }
    }

}