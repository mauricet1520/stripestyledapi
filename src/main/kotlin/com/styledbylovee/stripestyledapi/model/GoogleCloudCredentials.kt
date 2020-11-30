package com.styledbylovee.stripestyledapi.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class GoogleCloudCredentials(
        var type: String = "service_account",
     var privateKey: String,
     var projectId: String) {

    @Throws(JsonProcessingException::class)
    fun toJson(): ByteArray? {
        privateKey = this.privateKey.replace("\\n", System.lineSeparator())
        return jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .writeValueAsBytes(this)
    }
}