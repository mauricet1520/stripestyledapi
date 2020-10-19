package com.styledbylovee.stripestyledapi.model

data class StripeRequest (
        val amount: Int,
        val receipt_email: String,
        val token: String,
        val service: String
)