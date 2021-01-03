package com.styledbylovee.stripestyledapi.manager

import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.appointment.StyledCustomerAppointmentRequest
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

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
                val staffTimeSlots = StaffTimeSlots(it, slots)
                listOfStaffTimeSlots.add(checkAvailableTimeSlots(staffTimeSlots))
            }

        }
        return listOfStaffTimeSlots
    }

    fun createSetmoreCustomer(createCustomerRequest: CreateCustomerRequest?): CreateCustomerResponse {

        val globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

       return setmoreService.createSetmoreCustomer(globalToken, createCustomerRequest!!)
    }

    fun createAppointment(createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse = try {

        val globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

         setmoreService.createAppointment(globalToken, createAppointmentRequest)

    }catch (h: HttpClientErrorException) {
        throw h
    }

    fun updateAppointment(createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse{
        val globalToken = setmoreService.getAccessTokenIfFireBase().data.token.accessToken!!

        return setmoreService.updateAppointment(globalToken, createAppointmentRequest)
    }

    fun createCustomerAppointment(styledCustomerAppointmentRequest: StyledCustomerAppointmentRequest): CreateAppointmentResponse = try {
        val createCustomerResponse = createSetmoreCustomer(styledCustomerAppointmentRequest.createCustomerRequest)
        fireBaseService.saveSetmoreCustomerAppointment(styledCustomerAppointmentRequest)
        createAppointment(styledCustomerAppointmentRequest.createAppointmentRequest!!)
    } catch (h: HttpClientErrorException) {
        throw h
    }

    fun checkAvailableTimeSlots(staffTimeSlot: StaffTimeSlots): StaffTimeSlots {

            val sevenNineTimeSlots = HashSet<String>()
            val eightTenTimeSlots =  HashSet<String>()
            val nineElevenTimeSlots =  HashSet<String>()
            val tenTwelveTimeSlots =  HashSet<String>()
            val elevenThirteenTimeSlots =  HashSet<String>()
            val twelveFourteenTimeSlots =  HashSet<String>()
            val thirteenFifteenTimeSlots =  HashSet<String>()
            val fourteenSixteenTimeSlots =  HashSet<String>()

            staffTimeSlot.slots.forEach {
                when(it) {
                    "07.00"-> sevenNineTimeSlots.add("07.00")
                    "07.30"-> sevenNineTimeSlots.add("07.30")
                    "08.00"-> sevenNineTimeSlots.add("08.00")
                    "08.30"-> sevenNineTimeSlots.add("08.30")
                    "09.00"-> sevenNineTimeSlots.add("09.00")
                }

                when(it) {
                    "08.00"-> eightTenTimeSlots.add("08.00")
                    "08.30"-> eightTenTimeSlots.add("08.30")
                    "09.00"-> eightTenTimeSlots.add("09.00")
                    "09.30"-> eightTenTimeSlots.add("09.30")
                    "10.00"-> eightTenTimeSlots.add("10.00")
                }

                when(it) {
                    "09.00"-> nineElevenTimeSlots.add("09.00")
                    "09.30"-> nineElevenTimeSlots.add("09.30")
                    "10.00"-> nineElevenTimeSlots.add("10.00")
                    "10.30"-> nineElevenTimeSlots.add("10.30")
                    "11.00"-> nineElevenTimeSlots.add("11.00")
                }

                when(it) {
                    "10.00"-> tenTwelveTimeSlots.add("10.00")
                    "10.30"-> tenTwelveTimeSlots.add("10.30")
                    "11.00"-> tenTwelveTimeSlots.add("11.00")
                    "11.30"-> tenTwelveTimeSlots.add("11.30")
                    "12.00"-> tenTwelveTimeSlots.add("12.00")
                }

                when(it) {
                    "11.00"-> elevenThirteenTimeSlots.add("11.00")
                    "11.30"-> elevenThirteenTimeSlots.add("11.30")
                    "12.00"-> elevenThirteenTimeSlots.add("12.00")
                    "12.30"-> elevenThirteenTimeSlots.add("12.30")
                    "13.00"-> elevenThirteenTimeSlots.add("13.00")
                }

                when(it) {
                    "12.00"-> twelveFourteenTimeSlots.add("12.00")
                    "12.30"-> twelveFourteenTimeSlots.add("12.30")
                    "13.00"-> twelveFourteenTimeSlots.add("13.00")
                    "13.30"-> twelveFourteenTimeSlots.add("13.30")
                    "14.00"-> twelveFourteenTimeSlots.add("14.00")
                }

                when(it) {
                    "13.00"-> thirteenFifteenTimeSlots.add("13.00")
                    "13.30"-> thirteenFifteenTimeSlots.add("13.30")
                    "14.00"-> thirteenFifteenTimeSlots.add("14.00")
                    "14.30"-> thirteenFifteenTimeSlots.add("14.30")
                    "15.00"-> thirteenFifteenTimeSlots.add("15.00")
                }

                when(it) {
                    "14.00"-> fourteenSixteenTimeSlots.add("14.00")
                    "14.30"-> fourteenSixteenTimeSlots.add("14.30")
                    "15.00"-> fourteenSixteenTimeSlots.add("15.00")
                    "15.30"-> fourteenSixteenTimeSlots.add("15.30")
                    "16.00"-> fourteenSixteenTimeSlots.add("16.00")
                }
            }

            if (sevenNineTimeSlots.size >= 5) staffTimeSlot.sevenToNineTimeSlotsAvailable = true
            if (eightTenTimeSlots.size >= 5) staffTimeSlot.eightToTenTimeSlotsAvailable = true
            if (nineElevenTimeSlots.size >= 5) staffTimeSlot.nineToElevenTimeSlotsAvailable = true
            if (tenTwelveTimeSlots.size >= 5) staffTimeSlot.tenToTwelveTimeSlotsAvailable = true
            if (elevenThirteenTimeSlots.size >= 5) staffTimeSlot.elevenToThirteenTimeSlotsAvailable = true
            if (twelveFourteenTimeSlots.size >= 5) staffTimeSlot.twelveToFourteenTimeSlotsAvailable = true
            if (thirteenFifteenTimeSlots.size >= 5) staffTimeSlot.thirteenToFifteenTimeSlotsAvailable = true
            if (fourteenSixteenTimeSlots.size >= 5) staffTimeSlot.fourteenToSixteenTimeSlotsAvailable = true

        return staffTimeSlot
    }
}