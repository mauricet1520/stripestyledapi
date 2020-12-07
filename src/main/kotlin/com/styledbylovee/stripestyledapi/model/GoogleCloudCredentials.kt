package com.styledbylovee.stripestyledapi.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

data class GoogleCloudCredentials(
        var type: String = "service_account",
        @JsonProperty("client_email")
        var clientEmail: String,

        @JsonProperty("client_id")
        var clientId: String,

        @JsonAlias("private_key")
        var privateKey: String,

        var privateKeyId: String,

        @JsonProperty("project_id")
        var projectId: String) {


    @Throws(JsonProcessingException::class)
    fun toJson(): String? {
        privateKey = this.privateKey.replace("\\n", System.lineSeparator())

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
         val f =gson.toJson(this)

        println(f.toString())
        return f
    }
}