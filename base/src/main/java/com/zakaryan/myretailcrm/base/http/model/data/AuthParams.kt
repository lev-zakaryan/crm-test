package com.zakaryan.myretailcrm.base.http.model.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.zakaryan.myretailcrm.base.http.model.enum.GrantType
import com.zakaryan.myretailcrm.base.http.settings.ServerSettings

class AuthParams private constructor(
    @JsonProperty("grant_type")
    val grantType: GrantType,
    @JsonProperty("username")
    val user: String?,
    @JsonProperty("password")
    val pass: String?,
    @JsonProperty("refresh_token")
    val refreshToken: String?
) {

    @JsonProperty("client_id")
    val clientId: String = ServerSettings.CLIENT_ID

    @JsonProperty("client_secret")
    val clientSecret: String = ServerSettings.CLIENT_SECRET

    /* -------------------------------- COMPANION ----------------------------------------------- */

    companion object {

        fun fromPassword(user: String, pass: String): AuthParams {
            return AuthParams(GrantType.PASSWORD, user, pass, null)
        }

        fun fromRefreshToken(token: String): AuthParams {
            return AuthParams(GrantType.REFRESH, null, null, token)
        }

    }

}
