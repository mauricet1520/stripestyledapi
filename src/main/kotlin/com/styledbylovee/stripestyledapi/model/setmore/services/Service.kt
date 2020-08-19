package com.styledbylovee.stripestyledapi.model.setmore.services

import com.fasterxml.jackson.annotation.JsonProperty


data class Service (
    @JsonProperty("key")
    var key: String? = null,
    @JsonProperty("service_name")
    var serviceName: String? = null,
    @JsonProperty("staff_keys")
    var staffKeys: List<String>? = null,
    @JsonProperty("duration")
    var duration: Int? = null,
    @JsonProperty("buffer_duration")
    var bufferDuration: Int? = null,
    @JsonProperty("cost")
    var cost: Float? = null,
    @JsonProperty("currency")
    var currency: String? = null,
    @JsonProperty("image_url")
    var imageUrl: String? = null,
    @JsonProperty("description")
    var description: String? = null
    )