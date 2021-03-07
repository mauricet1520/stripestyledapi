package com.styledbylovee.stripestyledapi.manager

import com.fasterxml.jackson.databind.JsonNode
import com.styledbylovee.stripestyledapi.model.*
import com.styledbylovee.stripestyledapi.service.FireBaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.util.*

@Component
class FireBaseManager(@Autowired val fireBaseService: FireBaseService,
                      @Autowired val emailSender: JavaMailSender,
                      @Autowired val template: SimpleMailMessage,
                      @Value(value = "\${EMAIL_USERNAME}") val emailUsername: String,
                      @Value(value = "\${SALES_TAX}") val salesTax: Double) {

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

    fun authUser(fireBaseUser: FireBaseUser): ResponseEntity<FireBaseUserResponse> {

        return fireBaseService.authUser(fireBaseUser)
    }

    fun addCustomer(firebaseCustomer: FirebaseCustomer): FirebaseCustomer {

        return fireBaseService.addCustomer(firebaseCustomer)

    }

    fun addValidEmail(validZipCodeEmail: ValidZipCodeEmail) {
        template.setTo(emailUsername)
        template.setFrom("test@gmail.com")
        template.setSubject("Styled by LoveE New User")
        template.setText("The following email ${validZipCodeEmail.email} and zip ${validZipCodeEmail.zipCode} is valid")
        emailSender.send(template)

        fireBaseService.addValidEmail(validZipCodeEmail)
    }

    fun addInvalidEmail(invalidEmail: InvalidZipCodeEmail) {

        template.setTo(emailUsername)
        template.setFrom("test@gmail.com")
        template.setSubject("Styled by LoveE New User")
        template.setText("The following email ${invalidEmail.email} and zip ${invalidEmail.zipCode} is invalid")
        emailSender.send(template)

        fireBaseService.addInvalidEmail(invalidEmail)

    }

    fun getAppointment(appointmentId: String): Appointment {
        return fireBaseService.getAppointment(appointmentId)
    }

    fun getCustomer(uuid: String): FirebaseCustomer {
        return fireBaseService.getCustomer(uuid)
    }

    fun updateAppointment(appointment: Appointment) {
        fireBaseService.saveSetmoreCustomerAppointment(appointment)
    }

    fun getCustomerAppointment(customerId: String): MutableList<Appointment> {
        val customer = fireBaseService.getCustomer(customerId)
        val appointments = mutableListOf<Appointment>()
        customer.appointment_ids?.forEach {
            val app = fireBaseService.getAppointment(it)
            appointments.add(app)
        }
        return appointments
    }

    fun calculateProducts(costs: List<Double>): Double {
        var totalCost = 0.0
        costs.forEach {
            totalCost += it
        }
         val tax =  totalCost * salesTax
        return totalCost.plus(tax)
    }

    fun saveAllProducts(products: List<Product>) {
        val firebaseCustomer = getCustomer(products[0].firebase_customer_id)
        val firebaseAppointment = getAppointment(products[0].firebase_appointment_id)
        val transactionNumber = UUID.randomUUID()

        products.forEach {
            it.setmore_customer_key = firebaseCustomer.setmore_customer_id!!
            it.setmore_staff_key = firebaseAppointment.setmore_staff_key!!
            it.firebase_stylist_id = firebaseAppointment.setmore_staff_key!!
            it.setmore_service_key = firebaseAppointment.setmore_service_key!!
            it.transaction_number = transactionNumber.toString()
            fireBaseService.saveProduct(it)
        }
    }

    fun saveProductInTransaction(transaction: Transaction) {
        fireBaseService.saveProductInTransaction(transaction)
    }

    fun getProductsInTransaction(transactionNumber: String): Transaction? {

        var transaction: Transaction? = fireBaseService.getTransactionInFB(transactionNumber)
                ?: return Transaction(transaction_number = transactionNumber, totalCost = 0.0)

        val cost = mutableListOf<Double>()
        transaction?.products?.forEach {
            cost.add(it.cost)
        }
        val totalCost = calculateProducts(cost)
        transaction?.totalCost = totalCost

        return transaction
    }

    fun deleteProductInTransaction(transactionNumber: String, sku: String): Transaction? {
        val transaction = fireBaseService.deleteProductInTransaction(transactionNumber, sku)
        val cost = mutableListOf<Double>()
        transaction?.products?.forEach {
            cost.add(it.cost)
        }
        val totalCost = calculateProducts(cost)
        transaction?.totalCost = totalCost

        return transaction
    }

    fun getAllProducts(): JsonNode? {

        return fireBaseService.getAllProducts()
    }

    fun addEmailToMailChimp(mailChimpEmailRequest: MailChimpEmailRequest): ResponseEntity<JsonNode> {

        return fireBaseService.addEmailToMailChimp(mailChimpEmailRequest)
    }

}