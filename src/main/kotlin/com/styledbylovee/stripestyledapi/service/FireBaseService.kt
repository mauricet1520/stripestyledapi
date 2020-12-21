package com.styledbylovee.stripestyledapi.service

import com.google.auth.oauth2.AccessToken
import com.google.cloud.storage.*
import com.styledbylovee.stripestyledapi.config.FireBaseConfig
import com.styledbylovee.stripestyledapi.model.FireBaseUser
import com.styledbylovee.stripestyledapi.model.FireBaseUserResponse
import com.styledbylovee.stripestyledapi.model.FirebaseAppointment
import com.styledbylovee.stripestyledapi.model.Product
import com.styledbylovee.stripestyledapi.model.setmore.appointment.StyledCustomerAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.token.RefreshTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
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
                      @Autowired val fireBaseConfig: FireBaseConfig) {

    val logger = LoggerFactory.getLogger("FireBaseService")

    fun getZipCodes(): List<*>? {
        checkTokenExpDate()
        return restTemplate.getForObject("https://styled-by-love-e-qa.firebaseio.com/zipCodes.json?access_token=${accessToken.tokenValue}", List::class.java)
    }

    private fun checkTokenExpDate() {
        val tokenExpDate = accessToken.expirationTime
        val currentDate = Date()

        if (tokenExpDate < currentDate) {
            fireBaseConfig.initFireBase()
            logger.info("Current date $currentDate")
            logger.info("tokenExpDate  $tokenExpDate")
        }
    }


    fun getAccessToken(): RefreshTokenResponse {
        checkTokenExpDate()
        var fireBaseDatabaseAccessTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/accessToke.json?access_token="

        logger.info("Calling FetchAllServices Endpoint $fireBaseDatabaseAccessTokenUrl")

        return restTemplate.getForObject(fireBaseDatabaseAccessTokenUrl + accessToken.tokenValue, RefreshTokenResponse::class.java)!!
    }

    fun saveAccessToken(refreshTokenResponse: RefreshTokenResponse?) {
        checkTokenExpDate()

        var fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/accessToke.json?access_token="

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl + accessToken.tokenValue, HttpMethod.PUT, HttpEntity(refreshTokenResponse!!), String::class.java)
    }

    fun saveSetmoreCustomerAppointment(styledCustomerAppointmentRequest: StyledCustomerAppointmentRequest) {

        checkTokenExpDate()

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/appointments.json?access_token=${accessToken.tokenValue}"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(styledCustomerAppointmentRequest), StyledCustomerAppointmentRequest::class.java)
    }

    fun upLoadPhoto() {
        UploadObject.uploadObject("styled-by-love-e-qa", "styled-by-love-e-qa.appspot.com", "final", "src/main/resources/static/feel_confident.jpg")
    }

    fun saveAppointment(firebaseAppointment: FirebaseAppointment) {
        checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/customer_appointments.json?access_token=${accessToken.tokenValue}"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(firebaseAppointment), StyledCustomerAppointmentRequest::class.java)

    }


    fun authUser(fireBaseUser: FireBaseUser): ResponseEntity<FireBaseUserResponse> {
        checkTokenExpDate()
        val fireBaseDatabaseSaveTokenUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyBzmFvHq5kWmg7p7yQnoxSI-eNyHVYJR9w"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        return restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(fireBaseUser), FireBaseUserResponse::class.java)

    }

    fun saveProduct(product: Product) {

        val fireBaseDatabaseSaveTokenUrl = "https://styled-by-love-e-qa.firebaseio.com/product.json?access_token=${accessToken.tokenValue}"

        logger.info("Calling Endpoint $fireBaseDatabaseSaveTokenUrl")

        restTemplate.exchange(fireBaseDatabaseSaveTokenUrl, HttpMethod.POST, HttpEntity(product), StyledCustomerAppointmentRequest::class.java)
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