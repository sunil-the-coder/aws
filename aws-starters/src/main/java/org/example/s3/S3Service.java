package org.example.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.util.List;

public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public boolean createBucket(String bucketName) {
        try {

            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(bucketRequest);

            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            //Wait until the bucket is created & print.
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);

            System.out.println(bucketName + " is created and ready to use.");
            return true;

        } catch (S3Exception e) {
            //e.printStackTrace();
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    public boolean deleteBucket(String bucketName) {

        try {

            //We need to delete the entire objects inside given bucket to fully delete bucket.
            ListObjectsV2Request objectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
            ListObjectsV2Response listObjectsV2Response;

            do {

                //delete object inside each folder/object if nested directories.
                listObjectsV2Response = s3Client.listObjectsV2(objectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    System.out.println("Deleting object: " + s3Object.key());
                    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().key(s3Object.key()).build();
                    s3Client.deleteObject(deleteObjectRequest);
                }

            } while (listObjectsV2Response.isTruncated());

            DeleteBucketRequest delReq = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            System.out.println("Deleting bucket: " + bucketName);
            s3Client.deleteBucket(delReq);
            System.out.println(bucketName + " is deleted.");
            return true;
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    public List<S3Object> getObjects(String bucketName) {

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
            List<S3Object> contents = listObjectsResponse.contents();
            return contents;
        } catch (S3Exception e) {
            e.printStackTrace();
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return null;
    }
}
