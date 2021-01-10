package com.styledbylovee.stripestyledapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class EmailConfig {
    @Bean
    fun templateSimpleMessage(): SimpleMailMessage? {
        return SimpleMailMessage()
    }
}