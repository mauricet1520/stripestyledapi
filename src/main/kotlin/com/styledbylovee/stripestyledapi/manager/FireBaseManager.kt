package com.styledbylovee.stripestyledapi.manager

import com.styledbylovee.stripestyledapi.service.FireBaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FireBaseManager(@Autowired val fireBaseService: FireBaseService) {

    fun findZipCode(zipCode: Int): Boolean {
        val zipCodeList = fireBaseService.getZipCodes()

        zipCodeList!!.forEach {
            if (it == zipCode) return true
        }
        return false
    }

    fun uploadPhoto() {
        fireBaseService.upLoadPhoto()
    }
}