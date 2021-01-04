package com.styledbylovee.stripestyledapi.manager

import com.stripe.Stripe
import com.stripe.model.*
import com.stripe.param.PaymentIntentCreateParams
import com.styledbylovee.stripestyledapi.model.StripePaymentIntentRequest
import com.styledbylovee.stripestyledapi.model.StripeRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import java.util.*


@Component
class StripeManager(@Value(value = "\${stripeKey}") val stripeApiKey: String,
                    @Value(value = "\${subscriptionAmount}") val subscriptionAmount: Int,
                    @Value(value = "\${onDemandAmount}") val onDemandAmount: Int) {


    val logger = LoggerFactory.getLogger("StripeManager")

    fun createSubscription(stripeRequest: StripeRequest): String {

        Stripe.apiKey = stripeApiKey
        val customer = createCustomer(stripeRequest.receipt_email)
        val items: MutableList<Any> = ArrayList()
        val item1: MutableMap<String, Any> = HashMap()
        item1["price"] = createPrice().id
        items.add(item1)
        val params: MutableMap<String, Any> = HashMap()
        params["customer"] = customer.id
        createCard(customer, stripeRequest)
        params["items"] = items
        val subscription: Subscription = Subscription.create(params)
        return subscription.id
    }

    private fun createPrice(): Price {

        val recurring: MutableMap<String, Any> = HashMap()
        recurring["interval"] = "month"
        val params: MutableMap<String, Any> = HashMap()
        params["unit_amount"] = subscriptionAmount
        params["currency"] = "usd"
        params["recurring"] = recurring
        params["product"] = createProduct().id

        return Price.create(params)
    }

    private fun createProduct(): Product {

        val params: MutableMap<String, Any> = HashMap()
        params["name"] = "Styled Subscription"

        return Product.create(params)
    }

    private fun createCustomer(receiptEmail: String): Customer {

        val params: MutableMap<String, Any> = HashMap()
        params["description"] = "Styled Customer: $receiptEmail"
        params["email"] = receiptEmail

        return Customer.create(params)
    }

    private fun createCard(customer: Customer, stripeRequest: StripeRequest) {
        val params: MutableMap<String, Any> = HashMap()
        params["source"] = stripeRequest.token
        customer.sources.create(params) as Card
    }

    fun createCharge(stripeRequest: StripeRequest): String = try{
        logger.info("StripeRequest: $stripeRequest")

        Stripe.apiKey = stripeApiKey

        val params: MutableMap<String, Any> = HashMap()
        params["amount"] = stripeRequest.amount
        params["currency"] = "usd"
        params["source"] = stripeRequest.token
        params["receipt_email"] = stripeRequest.receipt_email
        params["description"] = "Service: ${stripeRequest.service}"
        Charge.create(params)
        createCustomer(stripeRequest.receipt_email).id
    } catch (h: Throwable) {
        logger.error(h.message, h)
        throw h
    }

    fun createPaymentIntent(stripePaymentIntentRequest: StripePaymentIntentRequest): String {
        Stripe.apiKey = stripeApiKey
        val paymentIntentCreateParams = PaymentIntentCreateParams.Builder()
                .setAmount(stripePaymentIntentRequest.amount)
                .setDescription("Payment for ${stripePaymentIntentRequest.email}")
                .setReceiptEmail(stripePaymentIntentRequest.email)
                .setCustomer(stripePaymentIntentRequest.email)
                .setCurrency("usd")
                .build()

        val paymentIntent = PaymentIntent.create(paymentIntentCreateParams)
        return paymentIntent.clientSecret
    }

}