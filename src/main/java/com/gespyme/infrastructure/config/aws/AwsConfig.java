package com.gespyme.infrastructure.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

@Configuration
public class AwsConfig {

  // @Value("${aws.roleArn}")
  private final String roleArn = "arn:aws:iam::442042545308:role/gespyme-s3";

  // @Value("${aws.rolename}")
  private final String roleSessionName = "SpringAppSession";

  // @Value("${aws.region}")
  private final Region region = Region.EU_NORTH_1;

  @Bean
  public StsAssumeRoleCredentialsProvider getCredentialsProvider() {
    StsClient stsClient = StsClient.builder().region(region).build();

    return StsAssumeRoleCredentialsProvider.builder()
        .stsClient(stsClient)
        .refreshRequest(
            requestBuilder -> requestBuilder.roleArn(roleArn).roleSessionName(roleSessionName))
        .build();
  }

  @Bean
  public S3Client s3Client(StsAssumeRoleCredentialsProvider credentialsProvider) {
    return S3Client.builder().region(region).credentialsProvider(credentialsProvider).build();
  }
}
