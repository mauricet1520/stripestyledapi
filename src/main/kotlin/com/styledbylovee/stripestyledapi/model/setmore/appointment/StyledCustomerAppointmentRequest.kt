package com.styledbylovee.stripestyledapi.model.setmore.appointment

import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerRequest

data class StyledCustomerAppointmentRequest(
        val createCustomerRequest: CreateCustomerRequest? = null,
        val createAppointmentRequest: CreateAppointmentRequest? = null)