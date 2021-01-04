package com.styledbylovee.stripestyledapi.controller

import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import com.styledbylovee.stripestyledapi.manager.StripeManager
import com.styledbylovee.stripestyledapi.model.StripePaymentIntentRequest
import com.styledbylovee.stripestyledapi.model.StripeRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StripeController(@Autowired val stripeManager: StripeManager) {

    @PostMapping("/createSubscription")
    fun createSubscription(@RequestBody stripeRequest: StripeRequest): String {
        return stripeManager.createSubscription(stripeRequest)
    }

    @PostMapping("/createCharge")
    fun createCharge(@RequestBody stripeRequest: StripeRequest): ResponseEntity<Any> = try {
        ResponseEntity(stripeManager.createCharge(stripeRequest), HttpStatus.OK)
    } catch (t: Throwable) {
        ResponseEntity(t.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }


    @PostMapping("/createPaymentIntent")
    fun createPaymentIntent(@RequestBody stripeRequest: StripePaymentIntentRequest): ResponseEntity<Any> = try {
        ResponseEntity(stripeManager.createPaymentIntent(stripeRequest), HttpStatus.OK)
    } catch (t: Throwable) {
        ResponseEntity(t.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }


}