package com.styledbylovee.stripestyledapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.google.auth.oauth2.AccessToken
import com.google.cloud.storage.*
import com.styledbylovee.stripestyledapi.config.FireBaseConfig
import com.styledbylovee.stripestyledapi.model.*
import com.styledbylovee.stripestyledapi.model.setmore.appointment.StyledCustomerAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.token.RefreshTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


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

    fun deleteProductInTransaction(transactionNumber: String, sku: String): Transaction? {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/transactions/${transactionNumber}.json?access_token=${token.tokenValue}"

        val transactionInFB = getTransactionInFB(transactionNumber)
        var productInFB = mutableListOf<Product>()

        if (transactionInFB?.products != null) {

            transactionInFB.products?.forEach {
                if (it.sku_number != sku) {
                    productInFB.add(it)
                }
            }

        }


        transactionInFB?.products = productInFB
        if (transactionInFB != null) {
            logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")
            restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(transactionInFB), StyledCustomerAppointmentRequest::class.java)
        }

        return transactionInFB
    }

    fun saveProductInTransaction(transaction: Transaction) {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/transactions/${transaction.transaction_number}.json?access_token=${token.tokenValue}"
        val transactionInFb = getTransactionInFB(transaction.transaction_number)
        var productsInTransaction = mutableListOf<Product>()


        if (transactionInFb?.products != null ) {
            productsInTransaction.addAll(transaction.products!!)
            productsInTransaction.addAll(transactionInFb.products!!)
            transactionInFb.products = productsInTransaction
            logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")
            restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(transactionInFb), StyledCustomerAppointmentRequest::class.java)
        } else {
            logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")
            restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.PUT, HttpEntity(transaction), StyledCustomerAppointmentRequest::class.java)
        }
    }

    fun getTransactionInFB(transactionNumber: String): Transaction? {
        val token = checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/transactions/$transactionNumber.json?access_token=${token.tokenValue}"
        val transaction = restTemplate.getForObject(fireBaseDatabaseSaveTokenUrl, Transaction::class.java)
        if (transaction != null) {
            return transaction
        }
        return null
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

    fun getAllProducts(): JsonNode? {
        val token = checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/product.json?access_token=${token.tokenValue}"

        logger.info("Calling Endpoint https://styled-by-love-e-qa.firebaseio.com/product.json?access_token=")

        return restTemplate.getForObject(fireBaseDatabaseSaveTokenUrl, JsonNode::class.java)

    }

    fun addEmailToMailChimp(mailChimpEmailRequest: MailChimpEmailRequest): ResponseEntity<JsonNode> {

        val mailChimpUrl = "https://us4.api.mailchimp.com/3.0/lists/b067bb4d60/members?skip_merge_validation=false"

        val headers = HttpHeaders()

        headers.add("Authorization", "apikey b24d0ff46eeec03747e29b8a6ca87322-us4")

        val entity = HttpEntity<Any>(mailChimpEmailRequest, headers)

        return restTemplate.exchange(mailChimpUrl, HttpMethod.POST, entity,JsonNode::class.java)
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