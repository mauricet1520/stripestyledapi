package com.styledbylovee.stripestyledapi.model.setmore

import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerResponse

data class CustomerAppointmentResponse (
        val customerResponse: CreateCustomerResponse,
        val appointmentResponse: CreateAppointmentResponse
)