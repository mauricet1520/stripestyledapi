package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.styledbylovee.stripestyledapi.model.GoogleCloudCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream

@Configuration
class GoogleCloudConfiguration(
        @Value(value = "\${gcp.privateKey}") val privateKey: String,
        @Value(value = "\${gcp.projectId}") val projectId: String)
{

    fun getGoogleCreds(): GoogleCredentials{
        return GoogleCredentials.fromStream(ByteArrayInputStream(getCreds().toJson()))
    }

    @Bean
    fun getCreds(): GoogleCloudCredentials {
        return GoogleCloudCredentials(
                privateKey = privateKey,
                projectId = projectId
        )
    }

}