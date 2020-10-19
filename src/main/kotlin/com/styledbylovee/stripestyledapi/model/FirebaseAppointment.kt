package com.styledbylovee.stripestyledapi.model


data class FirebaseAppointment(
        val address: String,
        val startTime: String,
        val endTime: String,
        val cost: Double,
        val event: String,
        val budget: String,
        val productIds: List<String>,
        val imageUrl: String,
        val setmoreAppointmentKey: String,
        val setmoreLabel: String,
        val setmoreServiceKey: String,
        val setmoreCustomerKey: String,
        val firebaseStylistId: String,
        val firebaseCustomerId: String

)