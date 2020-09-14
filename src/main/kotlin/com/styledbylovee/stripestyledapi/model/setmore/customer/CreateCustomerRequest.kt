package com.styledbylovee.stripestyledapi.model.setmore.customer

import com.fasterxml.jackson.annotation.JsonProperty


data class CreateCustomerRequest(
        @JsonProperty("key")
        var key: String? = null,

        @JsonProperty("first_name")
        var firstName: String? = null,
        @JsonProperty("last_name")
        var lastName: String? = null,
        @JsonProperty("email_id")
        var emailId: String? = null,
        @JsonProperty("country_code")
        var countryCode: String? = null,
        @JsonProperty("cell_phone")
        var cellPhone: String? = null,
        @JsonProperty("work_phone")
        var workPhone: String? = null,
        @JsonProperty("home_phone")
        var homePhone: String? = null,
        @JsonProperty("address")
        var address: String? = null,
        @JsonProperty("city")
        var city: String? = null,
        @JsonProperty("state")
        var state: String? = null,
        @JsonProperty("postal_code")
        var postalCode: String? = null,
        @JsonProperty("image_url")
        var imageUrl: String? = null,
        @JsonProperty("comment")
        var comment: String? = null,
        @JsonProperty("additional_fields")
        var additionalFields: AdditionalFields? = null
)

data class AdditionalFields(
        @JsonProperty("Skype_id")
        var skypeId: String? = null
)