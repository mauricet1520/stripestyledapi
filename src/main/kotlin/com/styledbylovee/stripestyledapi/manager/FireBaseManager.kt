package com.styledbylovee.stripestyledapi.manager

import com.styledbylovee.stripestyledapi.model.FirebaseAppointment
import com.styledbylovee.stripestyledapi.model.Product
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

    fun saveAppointment(appointment: FirebaseAppointment) {
        return fireBaseService.saveAppointment(appointment)
    }

    fun saveProduct(product: Product) {
        return fireBaseService.saveProduct(product)
    }

    fun uploadPhoto() {
        fireBaseService.upLoadPhoto()
    }
}