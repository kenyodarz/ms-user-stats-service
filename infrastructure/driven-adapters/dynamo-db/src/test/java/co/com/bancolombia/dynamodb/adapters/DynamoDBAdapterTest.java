package co.com.bancolombia.dynamodb.adapters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.bancolombia.dynamodb.model.UserStatsEntity;
import co.com.bancolombia.model.stats.UserStats;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ExtendWith(MockitoExtension.class)
class DynamoDBAdapterTest {

    private final UserStatsEntity userStatsEntity = UserStatsEntity.builder()
            .timestamp("2024-01-01T00:00:00Z")
            .hash("hash123")
            .totalContactoClientes(100)
            .build();
    private final UserStats userStats = UserStats.builder()
            .hash("hash123")
            .totalContactoClientes(100)
            .build();
    @Mock
    private DynamoDbEnhancedAsyncClient enhancedClient;
    @Mock
    private DynamoDbAsyncTable<UserStatsEntity> dynamoDbAsyncTable;
    @Mock
    private ObjectMapper mapper;
    private DynamoDBAdapter adapter;

    @BeforeEach
    void setUp() {
        when(enhancedClient.table("user_stats", TableSchema.fromBean(UserStatsEntity.class)))
                .thenReturn(dynamoDbAsyncTable);

        adapter = new DynamoDBAdapter(enhancedClient, mapper);
    }

    @Test
    void testSave() {
        when(mapper.map(userStats, UserStatsEntity.class)).thenReturn(userStatsEntity);
        when(dynamoDbAsyncTable.putItem(userStatsEntity)).thenReturn(
                CompletableFuture.completedFuture(null));

        StepVerifier.create(adapter.save(userStats))
                .expectNextMatches(saved -> saved.getHash().equals("hash123"))
                .verifyComplete();
    }

    @Test
    void testGetById() {
        when(dynamoDbAsyncTable.getItem(any(Key.class)))
                .thenReturn(CompletableFuture.completedFuture(userStatsEntity));
        when(mapper.map(userStatsEntity, UserStats.class)).thenReturn(userStats);

        StepVerifier.create(adapter.getById("2024-01-01T00:00:00Z"))
                .expectNext(userStats)
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(userStats, UserStatsEntity.class)).thenReturn(userStatsEntity);
        when(mapper.map(userStatsEntity, UserStats.class)).thenReturn(userStats);
        when(dynamoDbAsyncTable.deleteItem(userStatsEntity)).thenReturn(
                CompletableFuture.completedFuture(userStatsEntity));

        StepVerifier.create(adapter.delete(userStats))
                .expectNext(userStats)
                .verifyComplete();
    }
}
