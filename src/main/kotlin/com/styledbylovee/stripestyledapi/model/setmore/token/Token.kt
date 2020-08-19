package com.styledbylovee.stripestyledapi.model.setmore.token

import com.fasterxml.jackson.annotation.JsonProperty

data class Token(
        @JsonProperty("access_token")
        var accessToken: String? = null,
        var type: String? = null,
        var tokenType: String? = null,
        var expires: Long? = null,
        var issuedTo: String? = null,
        var scopes: List<String>? = null,
        var accessType: String? = null,
        var userId: String? = null,
        var projId: String? = null,
        var expiresIn: Long? = null
)