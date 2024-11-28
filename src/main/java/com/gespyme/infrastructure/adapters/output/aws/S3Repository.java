package com.gespyme.infrastructure.adapters.output.aws;

import java.io.IOException;
import java.io.InputStream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
@RequiredArgsConstructor
public class S3Repository {

  // @Value("${aws.bucketname}")
  private String bucketName = "gespyme-invoice";

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
}
