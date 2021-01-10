package com.styledbylovee.stripestyledapi.model

data class FirebaseCustomer (
        var uid: String? = null,
        var stripe_customer_id: String? = null,
        var setmore_customer_id: String? = null,
        var hubspot_customer_id: String? = null,

        var first_name: String? = null,
        var last_name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var address: String? = null,
        var city: String? = null,
        var state: String? = null,
        var zip: String? = null,

        var appointment_ids : MutableList<String>? = mutableListOf(),
        var product_ids : List<String>? = null,

        var image_url: String? = null,
        var height: String? = null,
        var body_type: MutableList<String>? = mutableListOf(),
        var clothing_type: String? = null,

        var bottom_size: String? = null,
        var dress_size: String? = null,
        var jumper_romper_size: String? = null,
        var top_size: String? = null,
        var blazer_size: String? = null,
        var dress_shirt_size: String? = null,
        var pants_size: String? = null,
        var shirt_size: String? = null,

        var preferred_stores: List<String>? = null,
        var colors: MutableList<String>? = mutableListOf(),
        var print_patterns: MutableList<String>? = mutableListOf()
)