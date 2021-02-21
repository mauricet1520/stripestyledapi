package com.styledbylovee.stripestyledapi.service

import com.google.auth.oauth2.AccessToken
import com.google.cloud.storage.*
import com.styledbylovee.stripestyledapi.config.FireBaseConfig
import com.styledbylovee.stripestyledapi.model.*
import com.styledbylovee.stripestyledapi.model.setmore.CustomerAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.appointment.StyledCustomerAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.token.RefreshTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


@Component
class FireBaseService(@Autowired val restTemplate: RestTemplate,
                      @Autowired val accessToken: AccessToken,
                      @Autowired val bucket: Bucket,
                      @Value(value = "\${FIREBASE_API_KEY}") val fireBaseApiKey: String,
                      @Autowired val fireBaseConfig: FireBaseConfig) {

    val logger = LoggerFactory.getLogger("FireBaseService")

    fun getZipCodes(): List<*>? {
        val token = checkTokenExpDate()
        return restTemplate.getForObject("https://styled-by-love-e-qa.firebaseio.com/zipCodes.json?access_token=${token.tokenValue}", List::class.java)
    }

    private fun checkTokenExpDate(): AccessToken {
        val tokenExpDate = accessToken.expirationTime
        val currentDate = Date()

        if (tokenExpDate < currentDate) {

            logger.info("Current date $currentDate")
            logger.info("tokenExpDate  $tokenExpDate")
            return fireBaseConfig.initFireBase()
        }
        return accessToken
    }


    fun getAccessToken(): RefreshTokenResponse {
        val token = checkTokenExpDate()
        val fireBaseDatabaseAccessTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/accessToke.json?access_token="

        logger.info("Calling FetchAllServices Endpoint $fireBaseDatabaseAccessTokenUrl")

        return restTemplate.getForObject(fireBaseDatabaseAccessTokenUrl + token.tokenValue, RefreshTokenResponse::class.java)!!
    }

    fun saveAccessToken(refreshTokenResponse: RefreshTokenResponse?) {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/accessToke.json?access_token="

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl + token.tokenValue, HttpMethod.PUT, HttpEntity(refreshTokenResponse!!), String::class.java)
    }

    fun saveSetmoreCustomerAppointment(appointment: Appointment) {

        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/appointments/${appointment.appointment_id}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/appointments/${appointment.appointment_id}.json?access_token=")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(appointment), Appointment::class.java)
    }

    fun upLoadPhoto() {
        UploadObject.uploadObject("styled-by-love-e-qa", "styled-by-love-e-qa.appspot.com", "final", "src/main/resources/static/feel_confident.jpg")
    }

    fun saveAppointment(firebaseAppointment: FirebaseAppointment) {
        val token = checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/customer_appointments.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(firebaseAppointment), StyledCustomerAppointmentRequest::class.java)

    }


    fun authUser(fireBaseUser: FireBaseUser): ResponseEntity<FireBaseUserResponse> {
        val token = checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        return restTemplate.exchange(fireBaseDatabaseSaveTokenUrl + fireBaseApiKey, HttpMethod.POST, HttpEntity(fireBaseUser), FireBaseUserResponse::class.java)

    }

    fun saveProduct(product: Product) {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/product.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(product), StyledCustomerAppointmentRequest::class.java)
    }

    fun saveProductInTransaction(transaction: Transaction) {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/transactions/${transaction.transaction_number}.json?access_token=${token.tokenValue}"
        val transactionInFb = getProductsInTransaction(transaction.transaction_number)
        val productInFB = ArrayList<Product>()
        if (transactionInFb != null) {
            productInFB.addAll(transactionInFb.products)
            productInFB.addAll(transaction.products)
            transactionInFb.products = productInFB
            logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")
            restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(transactionInFb), StyledCustomerAppointmentRequest::class.java)
        } else {
            logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")
            restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(transaction), StyledCustomerAppointmentRequest::class.java)
        }
    }

    fun getProductsInTransaction(transactionNumber: String): Transaction? {
        val token = checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/transactions/$transactionNumber.json?access_token=${token.tokenValue}"
        return restTemplate.getForObject(fireBaseDatabaseSaveTokenUrl, Transaction::class.java)
    }

    fun addCustomer(firebaseCustomer: FirebaseCustomer): FirebaseCustomer {

        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/customers/${firebaseCustomer.uid}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/customers/${firebaseCustomer.uid}.json?access_token=")

        return restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(firebaseCustomer), FirebaseCustomer::class.java).body!!

    }

    fun addValidEmail(validZipCodeEmail: ValidZipCodeEmail) {
        val token = checkTokenExpDate()

        val firstPartOfEmail = validZipCodeEmail.email.split("@")

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/email-list/${firstPartOfEmail[0]}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/email-list/${firstPartOfEmail[0]}.json?access_token=")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(validZipCodeEmail), ValidZipCodeEmail::class.java).body!!
    }

    fun addInvalidEmail(invalidEmail: InvalidZipCodeEmail) {

        val token = checkTokenExpDate()

        val firstPartOfEmail = invalidEmail.email.split("@")

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/invalid-email-list/${firstPartOfEmail[0]}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/invalid-email-list/${firstPartOfEmail[0]}.json?access_token=")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(invalidEmail), InvalidZipCodeEmail::class.java).body!!

    }

    fun getAppointment(appointmentId: String): Appointment {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/appointments/${appointmentId}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/appointments/${appointmentId}.json?access_token=")

        return restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.GET, HttpEntity(appointmentId), Appointment::class.java).body!!
    }

    fun getCustomer(uid: String): FirebaseCustomer {

        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/customers/${uid}.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/customers/${uid}.json?access_token=")

        return restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.GET, null, FirebaseCustomer::class.java).body!!
    }


    object UploadObject {
        @Throws(IOException::class)
        fun uploadObject(
                projectId: String?, bucketName: String, objectName: String, filePath: String) {

            val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service

            val blobId = BlobId.of(bucketName, objectName)
            val blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build()
            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)))
            println(
                    "File $filePath uploaded to bucket $bucketName as $objectName")
        }
    }

}