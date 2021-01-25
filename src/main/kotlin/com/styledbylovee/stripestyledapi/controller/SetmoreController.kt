package com.styledbylovee.stripestyledapi.controller

import com.styledbylovee.stripestyledapi.manager.SetmoreManager
import com.styledbylovee.stripestyledapi.model.Appointment
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.appointment.CreateAppointmentResponse
import com.styledbylovee.stripestyledapi.model.setmore.appointment.StyledCustomerAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.appointment.UpdateAppointmentRequest
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerRequest
import com.styledbylovee.stripestyledapi.model.setmore.customer.CreateCustomerResponse
import com.styledbylovee.stripestyledapi.model.setmore.services.FetchAllServicesResponse
import com.styledbylovee.stripestyledapi.model.setmore.staff.FetchAllStaffResponse
import com.styledbylovee.stripestyledapi.model.setmore.stafftimeslots.StaffTimeSlots
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException

@RestController
class SetmoreController(@Autowired val setmoreManager: SetmoreManager) {

    val logger = LoggerFactory.getLogger("SetmoreController")

    @GetMapping("/getStaffTimeSlots")
    fun getStaffTimeSlots(
            @RequestParam(value = "date") date: String,
            @RequestParam(value = "service") service: String): ResponseEntity<MutableList<StaffTimeSlots>> {
        logger.info("Request params $service on $date")
        return ResponseEntity(setmoreManager.getStaffTimeSlots(date, service), HttpStatus.OK)
    }

    @GetMapping("/getAllServices")
    fun getAllServices() : ResponseEntity<FetchAllServicesResponse> {
        return ResponseEntity(setmoreManager.fetchAllServices(), HttpStatus.OK)
    }

    @GetMapping("/getAllStaff")
    fun getAllStaff() : ResponseEntity<FetchAllStaffResponse> {
        return ResponseEntity(setmoreManager.fetchAllStaff(), HttpStatus.OK)
    }

    @PostMapping("/createSetmoreCustomer")
    fun createSetmoreCustomer(@RequestBody customerRequest: CreateCustomerRequest): CreateCustomerResponse {
        return setmoreManager.createSetmoreCustomer(customerRequest)
    }

    @PostMapping("/createAppointment")
    fun createSetmoreCustomer(@RequestBody createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse {
        return setmoreManager.createAppointment(createAppointmentRequest)
    }

    @PutMapping("/updateAppointment")
    fun updateAppointment(@RequestBody createAppointmentRequest: CreateAppointmentRequest): CreateAppointmentResponse {
        return setmoreManager.updateAppointment(createAppointmentRequest)
    }

    @PostMapping("createSetmoreCustomerAppointment")
    fun createCustomerAppointment(@RequestBody styledCustomerAppointmentRequest: StyledCustomerAppointmentRequest): Appointment {
        return setmoreManager.createCustomerAppointment(styledCustomerAppointmentRequest)
    }

    @GetMapping("/fetchAppointments")
    fun fetchAppointments(@RequestParam(value = "start_time") start_time: String, @RequestParam(value = "end_time") end_time: String): CreateAppointmentResponse {
        return setmoreManager.fetchAppointments(start_time, end_time)
    }

    @GetMapping("/fetchAllAppointments")
    fun fetchAllAppointments(@RequestParam(value = "start_time") start_time: String, @RequestParam(value = "end_time") end_time: String, @RequestParam(value = "staff_key") staff_key: String): CreateAppointmentResponse {
        return setmoreManager.fetchAllAppointments(start_time, end_time, staff_key)
    }

}