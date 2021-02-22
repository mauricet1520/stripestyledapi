package com.styledbylovee.stripestyledapi.controller

import com.fasterxml.jackson.databind.JsonNode
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


    @PostMapping("/inValidEmail")
    fun addInvalidEmail(@RequestBody invalidEmail: InvalidZipCodeEmail) {
        fireBaseManager.addInvalidEmail(invalidEmail)
    }

    @GetMapping("/getAppointment")
    fun getAppointment(@RequestParam(value = "appointmentId")appointmentId: String): Appointment {
        return fireBaseManager.getAppointment(appointmentId)
    }

    @GetMapping("/getCustomer")
    fun getCustomer(@RequestParam(value = "customerId")customerId: String): FirebaseCustomer {
        return fireBaseManager.getCustomer(customerId)
    }

    @GetMapping("/getCustomerAppointment")
    fun getCustomerAppointment(@RequestParam(value = "customerId")customerId: String): MutableList<Appointment> {
        return fireBaseManager.getCustomerAppointment(customerId)
    }

    @PostMapping("/updateAppointmentInFB")
    fun updateAppointment(@RequestBody appointment: Appointment) {
        return fireBaseManager.updateAppointment(appointment)
    }


    @PostMapping("/calculateProducts")
    fun calculateProducts(@RequestBody costs: List<Double>): Double {
        return fireBaseManager.calculateProducts(costs)
    }

    @PostMapping("/saveAllProducts")
    fun saveAllProducts(@RequestBody products: List<Product>) {
        return fireBaseManager.saveAllProducts(products)
    }

    @PostMapping("/saveProductInTransaction")
    fun saveProductInTransaction(@RequestBody transaction: Transaction) {
        return fireBaseManager.saveProductInTransaction(transaction)
    }

    @GetMapping("/getProductsInTransaction")
    fun getProductsInTransaction(@RequestParam("transaction_number") transactionNumber: String): Transaction? {
        return fireBaseManager.getProductsInTransaction(transactionNumber)
    }

    @PutMapping("/deleteProductInTransaction")
    fun deleteProductInTransaction(@RequestParam("transaction_number") transactionNumber: String,
                                   @RequestParam("sku") sku: String): Transaction? {
        return fireBaseManager.deleteProductInTransaction(transactionNumber, sku)
    }

    @GetMapping("/getAllProducts")
    fun getAllProducts(): Any {
        return fireBaseManager.getAllProducts() ?: Any()
    }

}