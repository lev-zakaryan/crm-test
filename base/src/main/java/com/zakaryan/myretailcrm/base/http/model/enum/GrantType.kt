package com.zakaryan.myretailcrm.base.http.model.enum

import com.fasterxml.jackson.annotation.JsonProperty

enum class GrantType {
    @JsonProperty("password")
    PASSWORD,
    @JsonProperty("refresh_token")
    REFRESH
}