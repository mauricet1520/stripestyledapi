package com.styledbylovee.stripestyledapi.model.setmore.customer

import com.fasterxml.jackson.annotation.JsonProperty


data class CreateCustomerResponse(
        @JsonProperty("response")
        var response: Boolean? = null,
        @JsonProperty("msg")
        var msg: String? = null,
        @JsonProperty("data")
        var data: Data? = null
)

data class Data(
        @JsonProperty("customer")
        var customer: CreateCustomerRequest? = null
)