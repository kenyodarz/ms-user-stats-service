package co.com.bancolombia.dynamodb.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

@ExtendWith(MockitoExtension.class)
class DynamoDBInitializerTest {

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @InjectMocks
    private DynamoDBInitializer initializer;

    @Test
    void shouldNotCreateTableWhenItAlreadyExists() throws Exception {
        ListTablesResponse response = ListTablesResponse.builder()
                .tableNames("user_stats") // ya existe
                .build();

        when(dynamoDbAsyncClient.listTables())
                .thenReturn(CompletableFuture.completedFuture(response));

        initializer.createTableIfNotExists();

        verify(dynamoDbAsyncClient, never()).createTable(any(CreateTableRequest.class));
    }

    @Test
    void shouldCreateTableWhenItDoesNotExist() throws Exception {
        ListTablesResponse response = ListTablesResponse.builder()
                .tableNames(new ArrayList<>()) // vacía
                .build();

        when(dynamoDbAsyncClient.listTables())
                .thenReturn(CompletableFuture.completedFuture(response));

        when(dynamoDbAsyncClient.createTable(any(CreateTableRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        initializer.createTableIfNotExists();

        verify(dynamoDbAsyncClient).createTable(any(CreateTableRequest.class));
    }

    @Test
    void shouldLogErrorWhenTableCreationFails() throws Exception {
        ListTablesResponse response = ListTablesResponse.builder()
                .tableNames(new ArrayList<>())
                .build();

        when(dynamoDbAsyncClient.listTables())
                .thenReturn(CompletableFuture.completedFuture(response));

        when(dynamoDbAsyncClient.createTable(any(CreateTableRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Falló la creación")));

        initializer.createTableIfNotExists();

        verify(dynamoDbAsyncClient).createTable(any(CreateTableRequest.class));
    }

}
