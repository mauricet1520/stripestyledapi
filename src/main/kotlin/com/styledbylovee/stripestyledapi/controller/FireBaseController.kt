package com.styledbylovee.stripestyledapi.controller

import com.styledbylovee.stripestyledapi.manager.FireBaseManager
import com.styledbylovee.stripestyledapi.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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

    @PostMapping("/product")
    fun addProduct(@RequestBody product: Product) {
        return fireBaseManager.saveProduct(product)
    }

    @PostMapping("/authNewUser")
    fun authNewUser(@RequestBody fireBaseUser: FireBaseUser): ResponseEntity<FireBaseUserResponse> {
        return fireBaseManager.authUser(fireBaseUser)
    }

    @PostMapping("/customer")
    fun addCustomer(@RequestBody firebaseCustomer: FirebaseCustomer): FirebaseCustomer {
        return fireBaseManager.addCustomer(firebaseCustomer)
    }

    @PostMapping("/validEmail")
    fun addValidEmail(@RequestBody validZipCodeEmail: ValidZipCodeEmail) {
        fireBaseManager.addValidEmail(validZipCodeEmail)
    }

}