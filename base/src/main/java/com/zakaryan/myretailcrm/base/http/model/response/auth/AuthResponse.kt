package com.zakaryan.myretailcrm.base.http.model.response.auth

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.zakaryan.myretailcrm.base.http.model.response.BaseResponse
import com.zakaryan.myretailcrm.base.json.JsonMapper

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthResponse(

    @JsonProperty("access_token")
    val accessToken: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("expires_in")
    val expiresIn: Long = 0,

    @JsonProperty("token_type")
    val tokenType: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("scope")
    val scope: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("refresh_token")
    val refreshToken: String = JsonMapper.EMPTY_STRING

) : BaseResponse()