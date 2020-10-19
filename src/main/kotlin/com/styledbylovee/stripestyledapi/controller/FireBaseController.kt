package com.styledbylovee.stripestyledapi.controller

import com.styledbylovee.stripestyledapi.manager.FireBaseManager
import com.styledbylovee.stripestyledapi.model.ActiveZipCode
import com.styledbylovee.stripestyledapi.model.FirebaseAppointment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class FireBaseController(@Autowired val fireBaseManager: FireBaseManager) {

    @GetMapping("/zipCode")
    fun findZipCode(@RequestParam(value = "zipCode") zipCode: Int): Boolean {
         return fireBaseManager.findZipCode(zipCode)
    }

    @PostMapping("/appointment")
    fun addAppointment(@RequestBody firebaseAppointment: FirebaseAppointment) {
        return fireBaseManager.saveAppointment(firebaseAppointment)
    }

}