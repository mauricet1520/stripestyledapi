package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.io.FileInputStream


@Configuration
class FireBaseConfig {
    @Bean
    fun initFireBase(): AccessToken {
        val serviceAccount = FileInputStream("src/main/resources/static/styled-by-love-e-qa-firebase-adminsdk-creds.json")

        val googleCred = GoogleCredentials.fromStream(serviceAccount)
        val scoped = googleCred.createScoped(listOf(
                "https://www.googleapis.com/auth/firebase.database",
                "https://www.googleapis.com/auth/userinfo.email"
        ))

        val options = FirebaseOptions.Builder()
                .setCredentials(googleCred)
                .setStorageBucket("styled-by-love-e-qa.appspot.com")
                .setDatabaseUrl("https://styled-by-love-e-qa.firebaseio.com")
                .build()
        FirebaseApp.initializeApp(options)

        scoped.refresh()

        return scoped.accessToken
    }

    @Bean
    fun bucket(): Bucket {
        return StorageClient.getInstance().bucket()
    }

    @Bean
    fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }
    }