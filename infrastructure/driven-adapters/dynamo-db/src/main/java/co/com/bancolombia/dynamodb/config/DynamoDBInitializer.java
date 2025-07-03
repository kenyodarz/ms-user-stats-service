package co.com.bancolombia.dynamodb.config;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DynamoDBInitializer {

    private static final String TABLE_NAME = "user_stats";
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @PostConstruct
    public void createTableIfNotExists() throws ExecutionException, InterruptedException {
        dynamoDbAsyncClient
                .listTables()
                .thenApply(ListTablesResponse::tableNames)
                .thenAccept(tableNames -> {
                    if (!tableNames.contains(TABLE_NAME)) {
                        log.info("Tabla '{}' no existe. Creándola...", TABLE_NAME);
                        createTable();
                    } else {
                        log.info("Tabla '{}' ya existe.", TABLE_NAME);
                    }
                })
                .get(); // Bloquea para asegurarse que termine antes de seguir
    }

    private void createTable() {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(TABLE_NAME)
                .keySchema(KeySchemaElement.builder()
                        .attributeName("timestamp")
                        .keyType(KeyType.HASH)
                        .build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("timestamp")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        dynamoDbAsyncClient.createTable(request)
                .thenAccept(response -> log.info("Tabla '{}' creada correctamente", TABLE_NAME))
                .exceptionally(e -> {
                    log.error("Error creando la tabla: {}", e.getMessage(), e);
                    return null;
                });
    }
}
