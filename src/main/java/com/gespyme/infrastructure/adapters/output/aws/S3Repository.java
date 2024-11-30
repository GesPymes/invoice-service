package com.gespyme.infrastructure.adapters.output.aws;

import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
@RequiredArgsConstructor
public class S3Repository {

  @Value("${aws.s3.bucketname}")
  private String bucketName;

  @Value("${aws.s3.pendingkeypath}")
  private String pendingKeyPath;

  @Value("${aws.s3.signedkeypath}")
  private String signedKeyPath;

  private final S3Client s3Client;

  public void uploadInvoiceToS3(byte[] pdfBytes, String invoiceName, String bucketPath) {
    String key = bucketPath + invoiceName;
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType("application/pdf")
            .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
  }

  public InputStream downloadInvoiceFromS3(String objectKey) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
    return s3Client.getObject(getObjectRequest);
  }

  public void deleteInvoiceFromS3(String objectKey) {
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build();
    s3Client.deleteObject(deleteObjectRequest);
  }
}
