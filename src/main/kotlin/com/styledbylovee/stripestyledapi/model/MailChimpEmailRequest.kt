package com.styledbylovee.stripestyledapi.model

data class MailChimpEmailRequest (
        var email_address: String,
        var status: String,
        var merge_fields: HashMap<String, String>
)