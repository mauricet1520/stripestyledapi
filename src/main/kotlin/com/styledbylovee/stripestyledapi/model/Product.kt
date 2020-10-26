package com.styledbylovee.stripestyledapi.model

data class Product (
        var date: String,
        var cost: Long,
        var transaction_number: Long,
        var store_name: String,
        var item_type: String,
        var item_image_url: String,
        var sku_image_url: String,

        var setmore_appointment_key: String,
        var setmore_staff_key: String,
        var setmore_service_key: String,
        var setmore_customer_key: String,
        var firebase_appointment_id: String,
        var firebase_stylist_id: String,
        var firebase_customer_id: String

)

