package com.styledbylovee.stripestyledapi.model

data class Transaction (
    val transaction_number: String,
    var products: List<Product>
)