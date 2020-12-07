package com.styledbylovee.stripestyledapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.styledbylovee.stripestyledapi.model.GoogleCloudCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.io.InputStream

@Configuration
class GoogleCloudConfiguration(
        @Value(value = "\${privateKey}") val privateKey: String,
        @Value(value = "\${projectId}") val projectId: String)
{

    fun getGoogleCreds(inputStream: InputStream): GoogleCredentials{
        return GoogleCredentials.fromStream(inputStream)
    }
}