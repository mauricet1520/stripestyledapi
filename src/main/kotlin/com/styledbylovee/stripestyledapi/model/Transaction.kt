package com.styledbylovee.stripestyledapi.model

data class Transaction (
    val transaction_number: String,
    var totalCost: Double,
    var products: MutableList<Product>? = null
)