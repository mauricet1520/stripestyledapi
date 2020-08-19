package com.styledbylovee.stripestyledapi.model.setmore.timeslots

import com.fasterxml.jackson.annotation.JsonProperty


data class FetchAllTimeSlotsRequest (
    @JsonProperty("staff_key")
    var staffKey: String? = null,
    @JsonProperty("service_key")
    var serviceKey: String? = null,
    @JsonProperty("selected_date")
    var selectedDate: String? = null,
    @JsonProperty("off_hours")
    var offHours: Boolean = false,
    @JsonProperty("double_booking")
    var doubleBooking: Boolean = false,
    @JsonProperty("slot_limit")
    var slotLimit: Int = 30,
    @JsonProperty("timezone")
    var timezone: String = "America/Los_Angeles"
)