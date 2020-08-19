package com.styledbylovee.stripestyledapi.model.setmore.appointment

import com.fasterxml.jackson.annotation.JsonProperty


data class Appointment (
    @JsonProperty("key")
    var key: String? = null,
    @JsonProperty("start_time")
    var startTime: String? = null,
    @JsonProperty("end_time")
    var endTime: String? = null,
    @JsonProperty("duration")
    var duration: Int? = null,
    @JsonProperty("staff_key")
    var staffKey: String? = null,
    @JsonProperty("service_key")
    var serviceKey: String? = null,
    @JsonProperty("customer_key")
    var customerKey: String? = null,
    @JsonProperty("cost")
    var cost: Int? = null,
    @JsonProperty("currency")
    var currency: String? = null,
    @JsonProperty("comment")
    var comment: String? = null,
    @JsonProperty("label")
    var label: String? = null
)