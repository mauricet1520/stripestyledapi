package com.styledbylovee.stripestyledapi.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.auth.oauth2.AccessToken
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.appointment.UpdateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerRequest
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerResponse
import com.styledbylovee.stripestyledapi.model.setmore.services.FetchAllServicesResponse
import com.styledbylovee.stripestyledapi.model.setmore.staff.FetchAllStaffResponse
import com.styledbylovee.stripestyledapi.model.setmore.timeslots.FetchAllTimeSlotsRequest
import com.styledbylovee.stripestyledapi.model.setmore.timeslots.FetchTimeSlotsResponse
import com.styledbylovee.stripestyledapi.model.setmore.token.RefreshTokenResponse
import org.apache.http.HttpException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class SetmoreService(
        @Autowired val restTemplate: RestTemplate,
        @Autowired val fireBaseService: FireBaseService,
        @Value(value = "\${refreshTokenValue}") val refreshTokenValue: String) {

    val logger = LoggerFactory.getLogger("SetmoreService")

    fun refreshToken(): RefreshTokenResponse {
        logger.info("Refreshing Token")

        val refreshTokenUrl = "https://developer.setmore.com/api/v1/o/oauth2/token?refreshToken="

        logger.info("Calling RefreshToken Endpoint $refreshTokenUrl")

        val refreshTokenResponse = restTemplate.getForObject(refreshTokenUrl + refreshTokenValue, RefreshTokenResponse::class.java)!!

        fireBaseService.saveAccessToken(refreshTokenResponse)

        logger.info("RefreshTokenResponse: ${refreshTokenResponse.data.token.accessToken}")

        return refreshTokenResponse
    }

    final fun getAccessTokenIfFireBase(): RefreshTokenResponse {

        val refreshTokenResponse = fireBaseService.getAccessToken()

        logger.info("Token from Firebase: ${refreshTokenResponse.data.token.accessToken!!}")

        return refreshTokenResponse
    }

    fun fetchAllServices(token: String): Pair<String, FetchAllServicesResponse> {

        try {
            val fetchAllServicesUrl = "https://developer.setmore.com/api/v1/bookingapi/services"

            logger.info("Calling FetchAllServices Endpoint $fetchAllServicesUrl")

            val jsonNodeResponse = restTemplate.exchange(fetchAllServicesUrl, HttpMethod.GET, createHeader(token), String::class.java)

            logger.info("FetchAllServicesResponse Status Code: ${jsonNodeResponse.statusCode} Value: ${jsonNodeResponse.statusCodeValue}")

            return Pair(token, jacksonObjectMapper().readValue(jsonNodeResponse.body, FetchAllServicesResponse::class.java))

        } catch (e: Throwable) {

            val fetchAllServicesUrl = "https://developer.setmore.com/api/v1/bookingapi/services"

            //Refresh Token
            val tokenRefresh = refreshToken().data.token.accessToken!!

            val fetchAllServicesResponse = restTemplate.exchange(
                    fetchAllServicesUrl,
                    HttpMethod.GET,
                    createHeader(tokenRefresh),
                    FetchAllServicesResponse::class.java)

            logger.info("FetchAllServicesResponse Status Code: ${fetchAllServicesResponse.statusCode} Value: ${fetchAllServicesResponse.statusCodeValue}")

            return Pair(tokenRefresh, fetchAllServicesResponse.body!!)
        }
    }

    fun fetchAllStaff(token: String): FetchAllStaffResponse = try {

        logger.info("Method: fetchAllStaff token: $token")

        val headers = HttpHeaders()

        headers.add("Authorization", "Bearer $token")

        val entity = HttpEntity<Any>(headers)

        val fetchAllStaffUrl = "https://developer.setmore.com/api/v1/bookingapi/staffs"

        logger.info("Calling FetchAllStaff Endpoint $fetchAllStaffUrl")

        val response = restTemplate.exchange(fetchAllStaffUrl, HttpMethod.GET, entity, FetchAllStaffResponse::class.java)

        logger.info("FetchAllStaffResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

        response.body!!

    } catch (e: HttpClientErrorException) {
        logger.error("FetchAllStaff failed", e)
        throw Exception("Failed while getting all staff")
    }

    fun fetchTimeSlots(token: String, fetchAllTimeSlotsRequest: FetchAllTimeSlotsRequest): FetchTimeSlotsResponse {
        logger.info("Method: fetchTimeSlots token $token")

        val fetchTimeSlotsUrl = "https://developer.setmore.com/api/v1/bookingapi/slots"

        return try {
            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $token")

            val entity = HttpEntity<Any>(fetchAllTimeSlotsRequest, headers)

            logger.info("Calling Services Endpoint $fetchTimeSlotsUrl")

            val response = restTemplate.exchange(fetchTimeSlotsUrl, HttpMethod.POST, entity, FetchTimeSlotsResponse::class.java)

            logger.info("FetchTimeSlotsResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!

        } catch (e: HttpClientErrorException) {
            throw Exception("Failure getting a token $token")
        } catch (x: Exception) {
            logger.error("Failure fetching time slots", x)

            return FetchTimeSlotsResponse(response = false)
        }
    }

    fun createSetmoreCustomer(token: String, createCustomerRequest: CreateCustomerRequest) : CreateCustomerResponse {
        logger.info("Method: createSetmoreCustomer token $token")

        val createCustomerUrl = "https://developer.setmore.com/api/v1/bookingapi/customer/create"

        return try {
            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $token")

            val entity = HttpEntity<Any>(createCustomerRequest, headers)

            logger.info("Calling Services Endpoint $createCustomerUrl")

            val response = restTemplate.exchange(createCustomerUrl, HttpMethod.POST, entity, CreateCustomerResponse::class.java)

            logger.info("CreateCustomerResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!

        } catch (e: HttpClientErrorException) {
            logger.error("Failure when calling create customer endpoint", e)

            val refreshTokenResponse = refreshToken()

            val freshToken = refreshTokenResponse.data.token.accessToken

            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $freshToken")

            val entity = HttpEntity<Any>(createCustomerRequest, headers)

            logger.info("Calling Services Endpoint $createCustomerUrl")

            val response = restTemplate.exchange(createCustomerUrl, HttpMethod.POST, entity, CreateCustomerResponse::class.java)

            logger.info("CreateCustomerResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!
        } catch (x: Exception) {
            logger.error("Failure creating Customer", x)

            return CreateCustomerResponse(response = false)
        }

    }

    fun getCustomerDetails(token: String): FetchAllStaffResponse = try {

        logger.info("Method: fetchAllStaff token: $token")

        val headers = HttpHeaders()

        headers.add("Authorization", "Bearer $token")

        val entity = HttpEntity<Any>(headers)

        val fetchAllStaffUrl = "https://developer.setmore.com/api/v1/bookingapi/staffs"

        logger.info("Calling FetchAllStaff Endpoint $fetchAllStaffUrl")

        val response = restTemplate.exchange(fetchAllStaffUrl, HttpMethod.GET, entity, FetchAllStaffResponse::class.java)

        logger.info("FetchAllStaffResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

        response.body!!

    } catch (e: HttpClientErrorException) {
        logger.error("FetchAllStaff failed", e)
        throw Exception("Failed while getting all staff")
    }

    fun createAppointment(token: String, createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse {

        logger.info("Method: createSetmoreCustomer token $token")

        val createAppointmentUrl = "https://developer.setmore.com/api/v1/bookingapi/appointment/create"

        return try {
            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $token")

            val entity = HttpEntity<Any>(createAppointmentRequest, headers)

            logger.info("Calling Services Endpoint $createAppointmentUrl")

            val response = restTemplate.exchange(createAppointmentUrl, HttpMethod.POST, entity, CreateAppointmentResponse::class.java)

            logger.info("CreateAppointmentResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!

        } catch (e: HttpClientErrorException) {
            logger.error("Failure when calling create customer endpoint", e)

            val refreshTokenResponse = refreshToken()

            val freshToken = refreshTokenResponse.data.token.accessToken

            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $freshToken")

            val entity = HttpEntity<Any>(createAppointmentRequest, headers)

            logger.info("Calling Services Endpoint $createAppointmentUrl")

            try {

                val response = restTemplate.exchange(createAppointmentUrl, HttpMethod.POST, entity, CreateAppointmentResponse::class.java)

                logger.info("CreateAppointmentResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

                response.body!!
            }catch (h: HttpClientErrorException) {
                logger.error(h.message, h)
                throw h
            }


        }
        catch (x: Exception) {
            logger.error("Failure creating Appointment", x)

            throw Throwable(x)
        }

    }

    fun updateAppointment(token: String, createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse {

        logger.info("Method: updateAppointment token $token")

        val updateAppointmentUrl = "https://developer.setmore.com/api/v1/bookingapi/appointments/${createAppointmentRequest.appointmentKey}/label"

        return try {
            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $token")

            val entity = HttpEntity<Any>(createAppointmentRequest, headers)

            logger.info("Calling Services Endpoint $updateAppointmentUrl")

            val response = restTemplate.exchange(updateAppointmentUrl, HttpMethod.PUT, entity, CreateAppointmentResponse::class.java)

            logger.info("CreateAppointmentResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!

        } catch (e: HttpClientErrorException) {
            logger.error("Failure when calling update appointment endpoint", e)

            val refreshTokenResponse = refreshToken()

            val freshToken = refreshTokenResponse.data.token.accessToken

            val headers = HttpHeaders()

            headers.add("Authorization", "Bearer $freshToken")

            val entity = HttpEntity<Any>(createAppointmentRequest, headers)

            logger.info("Calling Services Endpoint $updateAppointmentUrl")

            val response = restTemplate.exchange(updateAppointmentUrl, HttpMethod.PUT, entity, CreateAppointmentResponse::class.java)

            logger.info("UpdateAppointmentResponse Status Code: ${response.statusCode} Value: ${response.statusCodeValue}")

            response.body!!
        } catch (x: Exception) {
            logger.error("Failure updating Appointment", x)

            return CreateAppointmentResponse(response = false)
        }

    }

}

private fun createHeader(token: String): HttpEntity<Any> {

    val headers = HttpHeaders()

    headers.add("Authorization", "Bearer $token")

    val entity = HttpEntity<Any>(headers)

    return entity
}

data class NoTimeSlotsErrorResponse(val response: Boolean, val msg: String)