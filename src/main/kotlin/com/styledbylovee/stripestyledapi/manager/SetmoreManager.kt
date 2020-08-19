package com.styledbylovee.stripestyledapi.manager

import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerRequest
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerResponse
import com.styledbylovee.stripestyledapi.model.setmore.services.FetchAllServicesResponse
import com.styledbylovee.stripestyledapi.model.setmore.staff.FetchAllStaffResponse
import com.styledbylovee.stripestyledapi.model.setmore.stafftimeslots.StaffTimeSlots
import com.styledbylovee.stripestyledapi.model.setmore.timeslots.FetchAllTimeSlotsRequest
import com.styledbylovee.stripestyledapi.service.FireBaseService
import com.styledbylovee.stripestyledapi.service.SetmoreService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException

@Component
class SetmoreManager(@Autowired val setmoreService: SetmoreService, @Autowired val fireBaseService: FireBaseService) {

    val logger = LoggerFactory.getLogger("SetmoreManager")

    fun fetchAllServices(): FetchAllServicesResponse {
        return try {

            val token = fireBaseService.getAccessToken().data.token.accessToken!!

            val response = setmoreService.fetchAllServices(token)

            response.second

        } catch (e: HttpClientErrorException) {

            logger.error("Failed to use token from fire base")

            val token = setmoreService.refreshToken().data.token.accessToken!!

            val response = setmoreService.fetchAllServices(token)

            response.second
        }
    }

    fun fetchAllStaff(): FetchAllStaffResponse {
        return try {

            val token = fireBaseService.getAccessToken().data.token.accessToken!!

            setmoreService.fetchAllStaff(token)

        } catch (e: HttpClientErrorException) {

            logger.error("Failed to use token from fire base")

            val token = setmoreService.refreshToken().data.token.accessToken!!

            setmoreService.fetchAllStaff(token)
        }
    }

    fun getStaffTimeSlots(date: String, serviceName: String): MutableList<StaffTimeSlots> {

        var globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

        val fetchAllServicesResponsePair = setmoreService.fetchAllServices(globalToken)

        val mapOfTimeSlots = mutableMapOf<String, List<String>?>()

        val listOfStaffTimeSlots = mutableListOf<StaffTimeSlots>()

        var timesServiceSearched = 0

        globalToken = fetchAllServicesResponsePair.first


        run loop@{
            //Loop to find all services
            fetchAllServicesResponsePair.second.data.services?.forEach lit@{ service ->

                timesServiceSearched++

                logger.info("Looping service${service.serviceName} round: $timesServiceSearched")

                if (service.serviceName == serviceName) {

                    service.staffKeys!!.forEach { staffKey ->

                        logger.info("Fetching timeSlot for staff: $staffKey on $date")

                        val fetchAllTimeSlotsRequest = FetchAllTimeSlotsRequest(
                                staffKey = staffKey,
                                serviceKey = service.key,
                                selectedDate = date
                        )

                        val fetchTimeSlotsResponse = setmoreService.fetchTimeSlots(globalToken, fetchAllTimeSlotsRequest)

                        if (fetchTimeSlotsResponse.response) {
                            mapOfTimeSlots[staffKey] = fetchTimeSlotsResponse.data?.slots
                        }
                    }
                    return@loop
                }
            }
        }

        val fetchAllStaffResponse = setmoreService.fetchAllStaff(globalToken)

        fetchAllStaffResponse.data.staffs.forEach {
            val slots = mapOfTimeSlots[it.key]

            if (slots != null) {
                listOfStaffTimeSlots.add(StaffTimeSlots(it, slots))
            }

        }
        return listOfStaffTimeSlots
    }

    fun createSetmoreCustomer(createCustomerRequest: CreateCustomerRequest): CreateCustomerResponse {

        val globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

       return setmoreService.createSetmoreCustomer(globalToken, createCustomerRequest)
    }

    fun createAppointment(createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse {

        val globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

        return setmoreService.createAppointment(globalToken, createAppointmentRequest)
    }
}