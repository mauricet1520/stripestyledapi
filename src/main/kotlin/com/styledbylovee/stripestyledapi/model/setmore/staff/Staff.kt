package com.styledbylovee.stripestyledapi.model.setmore.staff

import com.fasterxml.jackson.annotation.JsonProperty


data class Staff (
    @JsonProperty("key")
    var key: String? = null,
    @JsonProperty("company_key")
    var companyKey: String? = null,
    @JsonProperty("contact_type")
    var contactType: String? = null,
    @JsonProperty("first_name")
    var firstName: String? = null,
    @JsonProperty("email_id")
    var emailId: String? = null,
    @JsonProperty("country_code")
    var countryCode: String? = null,
    @JsonProperty("image_url")
    var imageUrl: String? = null
    )