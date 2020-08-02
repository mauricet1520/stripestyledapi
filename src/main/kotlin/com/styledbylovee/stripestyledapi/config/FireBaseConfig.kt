package com.styledbylovee.stripestyledapi.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FireBaseConfig {
    @Bean
    fun initFireBase() {
        val serviceAccount = FileInputStream("src/main/resources/static/styled-by-love-e-qa-firebase-adminsdk-creds.json")

        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://styled-by-love-e-qa.firebaseio.com")
                .build()
        FirebaseApp.initializeApp(options)

        }
    }