package com.zakaryan.myretailcrm.base.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

object JsonMapper {

    const val EMPTY_STRING = ""
    const val INVALID_INTEGER = -1

    val instance: ObjectMapper = buildMapper()

    private fun buildMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule(nullisSameAsDefault = true))
        objectMapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP))
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        objectMapper.configure(
            DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true
        )
        return objectMapper
    }

}