package com.styledbylovee.stripestyledapi.model.setmore.appointment

import com.fasterxml.jackson.annotation.JsonProperty


data class CreateAppointmentResponse (
    @JsonProperty("response")
    var response: Boolean? = null,
    @JsonProperty("msg")
    var msg: String? = null,
    @JsonProperty("data")
    var data: Data? = null
)

data class Data (
    @JsonProperty("appointment")
    var appointment: Appointment? = null
)