import org.example.s3.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.UUID;

public class S3ServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public static void main(String[] args) {
        S3Client s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        /* 1. Create the New Bucket */
        final String bucketName = "sdk-" + UUID.randomUUID();
        S3Service s3Service = new S3Service(s3Client);
        s3Service.createBucket(bucketName);

        /* 2. Upload objects to bucket */


        /* 3. List objects from bucket */
        List<S3Object> objects = s3Service.getObjects(bucketName);
        System.out.println("Objects inside : " + bucketName);
        for (S3Object obj : objects) {
            System.out.println("Name:" + obj.key());
            System.out.println("Size:" + obj.size() + " bytes");
        }

        /* 4. Delete the bucket */
        s3Service.deleteBucket(bucketName);

        /* cleanup pool */
        s3Client.close();
    }
}
