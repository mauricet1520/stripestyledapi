package com.styledbylovee.stripestyledapi.model

data class Appointment(
        var appointment_id: String? = null,

        var street_address: String? = null,
        var city: String? = null,
        var zip: String? = null,
        var state: String? = null,
        var occasion: String? = null,

        var start_time: String? = null,
        var end_time: String? = null,
        var cost: Long? = null,
        var event: String? = null,
        var budget: String? = null,
        var product_ids: List<String>? = null,
        var image_url: String? = null,

        var setmore_appointment_key: String? = null,
        var setmore_service_name: String? = null,
        var setmore_label: String? = null,
        var setmore_staff_key: String? = null,
        var setmore_service_key: String? = null,
        var setmore_customer_key: String? = null,
        var stylist_id: String? = null,
        var customer_id: String? = null,
        var status: String? = null,
        var user_appointment_time: String? = null
)