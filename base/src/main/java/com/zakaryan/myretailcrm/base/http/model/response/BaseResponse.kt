package com.zakaryan.myretailcrm.base.http.model.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.zakaryan.myretailcrm.base.json.JsonMapper

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class BaseResponse {

    @JsonProperty("error")
    val error: String = JsonMapper.EMPTY_STRING

    @JsonProperty("error_description")
    val errorDescription: String = JsonMapper.EMPTY_STRING

    val isSuccessful: Boolean = error == JsonMapper.EMPTY_STRING

}