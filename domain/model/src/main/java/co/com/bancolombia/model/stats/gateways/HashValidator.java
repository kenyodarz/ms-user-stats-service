package co.com.bancolombia.model.stats.gateways;

import co.com.bancolombia.model.stats.UserStats;

public interface HashValidator {
    boolean isValid(UserStats stats);
}
