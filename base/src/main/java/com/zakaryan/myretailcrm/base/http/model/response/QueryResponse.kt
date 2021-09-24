package com.zakaryan.myretailcrm.base.http.model.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class QueryResponse : BaseResponse()