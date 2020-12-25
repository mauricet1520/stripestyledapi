package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import java.util.Base64.getDecoder


@Configuration
class FireBaseConfig {

    @Autowired
    lateinit var googleCloudConfiguration: GoogleCloudConfiguration

    val logger = LoggerFactory.getLogger("FireBaseConfig")


    @Bean
    @Scope("prototype")
    fun initFireBase(): AccessToken {
        val creds = decodeCredentials(googleCloudConfiguration.privateKey)
        val scoped = creds.createScoped(listOf(
                "https://www.googleapis.com/auth/firebase.database",
                "https://www.googleapis.com/auth/userinfo.email"
        ))

        try {
            FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME)
            logger.info("Refresh Token")
            return scoped.refreshAccessToken()
        }catch (e: Exception) {
            logger.error("Error: ${e.message}")
        }


        val options = FirebaseOptions.Builder()
                .setCredentials(creds)
                .setStorageBucket("styled-by-love-e-qa.appspot.com")
                .setDatabaseUrl("https://styled-by-love-e-qa.firebaseio.com")
                .build()

        FirebaseApp.initializeApp(options)
        return scoped.refreshAccessToken()
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