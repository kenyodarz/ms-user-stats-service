package co.com.bancolombia.dynamodb.adapters;

import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.dynamodb.model.UserStatsEntity;
import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import java.time.Instant;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Log
@Repository
public class DynamoDBAdapter extends TemplateAdapterOperations<UserStats, String, UserStatsEntity>
        implements StatsRepository {

    public DynamoDBAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper) {
        super(client, mapper, entity -> mapper.map(entity, UserStats.class), "user_stats");
    }

    // Sobrescribes solo toEntity para inyectar timestamp automáticamente
    @Override
    protected UserStatsEntity toEntity(UserStats stats) {
        UserStatsEntity entity = mapper.map(stats, UserStatsEntity.class);
        entity.setTimestamp(Instant.now().toString());
        return entity;
    }
}