package com.styledbylovee.stripestyledapi.manager

import com.styledbylovee.stripestyledapi.model.*
import com.styledbylovee.stripestyledapi.service.FireBaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class FireBaseManager(@Autowired val fireBaseService: FireBaseService,
                      @Autowired val emailSender: JavaMailSender,
                      @Autowired val template: SimpleMailMessage,
                      @Value(value = "\${EMAIL_USERNAME}") val emailUsername: String) {

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
}