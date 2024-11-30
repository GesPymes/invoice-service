package com.gespyme.infrastructure.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

@Configuration
public class AwsConfig {

  @Value("${aws.roleArn}")
  private  String roleArn;

  @Value("${aws.region}")
  private  String region;

  @Value("${aws.roleSessionName}")
  private  String roleSessionName;

  @Bean
  public StsAssumeRoleCredentialsProvider getCredentialsProvider() {
    StsClient stsClient = StsClient.builder().region(Region.of(region)).build();

    return StsAssumeRoleCredentialsProvider.builder()
        .stsClient(stsClient)
        .refreshRequest(
            requestBuilder -> requestBuilder.roleArn(roleArn).roleSessionName(roleSessionName))
        .build();
  }

  @Bean
  public S3Client s3Client(StsAssumeRoleCredentialsProvider credentialsProvider) {
    return S3Client.builder().region(Region.of(region)).credentialsProvider(credentialsProvider).build();
  }
}
