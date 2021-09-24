package com.zakaryan.myretailcrm.base.http.model.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.zakaryan.myretailcrm.base.json.JsonMapper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(

    @JsonProperty("id")
    val id: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("firstName")
    val firstName: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("lastName")
    val lastName: String = JsonMapper.EMPTY_STRING,

    @JsonProperty("patronymic")
    val patronymic: String = JsonMapper.EMPTY_STRING

) : Parcelable