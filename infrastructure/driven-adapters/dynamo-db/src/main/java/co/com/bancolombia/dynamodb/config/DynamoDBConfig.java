package co.com.bancolombia.dynamodb.config;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Slf4j
@Configuration
public class DynamoDBConfig {

    @Bean
    @Profile("local")
    public DynamoDbAsyncClient localDynamoClient(
            @Value("${aws.dynamodb.endpoint}") final String endpoint,
            @Value("${aws.region}") final String region,
            @Value("${aws.dynamodb.access-key-id}") final String accessKey,
            @Value("${aws.dynamodb.secret-access-key}") final String secretKey
    ) {
        log.info("🟡 DynamoDB AccessKey: {}", accessKey);
        log.info("🟡 DynamoDB Region: {}", region);
        log.info("🟡 DynamoDB Endpoint: {}", endpoint);
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    @Bean
    @Profile({"dev", "cer", "pdn"})
    public DynamoDbAsyncClient cloudDynamoClient(
            @Value("${aws.region}") String region
    ) {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient enhancedClient(DynamoDbAsyncClient client) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(client)
                .build();
    }
}
