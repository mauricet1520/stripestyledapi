package com.styledbylovee.stripestyledapi.service

import com.google.auth.oauth2.AccessToken
import com.google.cloud.storage.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


@Component
class FireBaseService(@Autowired val restTemplate: RestTemplate,
                      @Autowired val accessToken: AccessToken,
                      @Autowired val bucket: Bucket) {


    fun getZipCodes(): List<*>? {
        return restTemplate.getForObject("https://styled-by-love-e-qa.firebaseio.com/zipCodes.json?access_token=${accessToken.tokenValue}", List::class.java)
    }

    fun upLoadPhoto() {
        UploadObject.uploadObject("styled-by-love-e-qa", "styled-by-love-e-qa.appspot.com", "final", "src/main/resources/static/feel_confident.jpg")
    }

    object UploadObject {
        @Throws(IOException::class)
        fun uploadObject(
                projectId: String?, bucketName: String, objectName: String, filePath: String) { // The ID of your GCP project
// String projectId = "your-project-id";
// The ID of your GCS bucket
// String bucketName = "your-unique-bucket-name";
// The ID of your GCS object
// String objectName = "your-object-name";
// The path to your file to upload
// String filePath = "path/to/your/file"
            val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service

            val blobId = BlobId.of(bucketName, objectName)
            val blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build()
            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)))
            println(
                    "File $filePath uploaded to bucket $bucketName as $objectName")
        }
    }

}