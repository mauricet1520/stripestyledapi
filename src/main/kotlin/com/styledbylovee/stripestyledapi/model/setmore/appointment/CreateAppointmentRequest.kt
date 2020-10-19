package com.styledbylovee.stripestyledapi.model.setmore.appointment

import com.fasterxml.jackson.annotation.JsonProperty


data class CreateAppointmentRequest (
    @JsonProperty("staff_key")
    var staffKey: String? = null,
    @JsonProperty("service_key")
    var serviceKey: String? = null,
    @JsonProperty("customer_key")
    var customerKey: String? = null,
    @JsonProperty("start_time")
    var startTime: String? = null,
    @JsonProperty("end_time")
    var endTime: String? = null,
    @JsonProperty("comment")
    var comment: String? = null,
    @JsonProperty("label")
    var label: String,
    @JsonProperty("appointmentKey")
    var appointmentKey: String? = null
)
