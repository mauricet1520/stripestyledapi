package com.styledbylovee.stripestyledapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class FireBaseService(@Autowired val restTemplate: RestTemplate) {

    fun getZipCodes(): List<*>? {
        return restTemplate.getForObject("https://styled-by-love-e-qa.firebaseio.com/zipCodes.json", List::class.java)
    }

}