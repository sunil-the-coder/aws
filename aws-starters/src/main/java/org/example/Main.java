package org.example;

import org.example.s3.S3Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class Main {
    public static void main(String[] args) {

        S3Client s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        /* 1. Create the New Bucket */
        final String bucketName = "bucket-from-javasdk-2";
        S3Service s3Service = new S3Service(s3Client);
        s3Service.createBucket(bucketName);

        /* 2. Upload objects to bucket */


        /* 3. List objects from bucket */


        /* 4. Delete the bucket */

    }
}