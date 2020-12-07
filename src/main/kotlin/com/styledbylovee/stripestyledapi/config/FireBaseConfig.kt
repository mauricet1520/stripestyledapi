package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import java.util.Base64.getDecoder


@Configuration
class FireBaseConfig {

    @Autowired
    lateinit var googleCloudConfiguration: GoogleCloudConfiguration
    @Bean
    fun initFireBase(): AccessToken {
/*
        val serviceAccount = FileInputStream("src/main/resources/static/styled-by-love-qa-firebase-adminsdk-creds.json")
*/


//        val scoped = creds.createScoped(listOf(
//                "https://www.googleapis.com/auth/firebase.database",
//                "https://www.googleapis.com/auth/userinfo.email"
//        ))

        val creds = decodeCredentials(googleCloudConfiguration.privateKey)
        val scoped = creds.createScoped(listOf(
                "https://www.googleapis.com/auth/firebase.database",
                "https://www.googleapis.com/auth/userinfo.email"
        ))



        val options = FirebaseOptions.Builder()
                .setCredentials(creds)
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

    @Throws(IOException::class)
    private fun decodeCredentials(privateKey: String): GoogleCredentials {
        val byteArrayInputStream = ByteArrayInputStream(getDecoder().decode(privateKey))
        val creds = googleCloudConfiguration.getGoogleCreds(byteArrayInputStream)

        return creds
    }
    }