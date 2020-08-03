package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
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

    @Bean
    fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }
    }