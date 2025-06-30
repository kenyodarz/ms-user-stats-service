package co.com.bancolombia.dynamodb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ExtendWith(MockitoExtension.class)
class DynamoDBConfigTest {

    private final DynamoDBConfig config = new DynamoDBConfig();

    @Test
    void testLocalDynamoClient() {
        DynamoDbAsyncClient client = config.localDynamoClient("http://localhost:8000", "us-east-1");
        assertNotNull(client);
    }

    @Test
    void testCloudDynamoClient() {
        DynamoDbAsyncClient client = config.cloudDynamoClient("us-east-1");
        assertNotNull(client);
    }

    @Test
    void testEnhancedClient() {
        DynamoDbAsyncClient baseClient = config.localDynamoClient("http://localhost:8000",
                "us-east-1");
        DynamoDbEnhancedAsyncClient enhancedClient = config.enhancedClient(baseClient);
        assertNotNull(enhancedClient);
    }
}
