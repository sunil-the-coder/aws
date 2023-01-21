package org.example.s3;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public class S3Service {

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

        }catch(S3Exception e) {
            //e.printStackTrace();
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    public boolean deleteBucket(String bucketName) {

        DeleteBucketPolicyRequest delReq = DeleteBucketPolicyRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.deleteBucketPolicy(delReq);
            System.out.println(bucketName +" is deleted.");
            return true;
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }
    }
}
