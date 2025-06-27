package co.com.bancolombia.dynamodb.util;

import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.model.stats.gateways.HashValidator;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@Slf4j
public class HashValidatorImpl implements HashValidator {

    @Override
    public boolean isValid(UserStats stats) {
        String raw = String.format("%d,%d,%d,%d,%d,%d,%d",
                stats.getTotalContactoClientes(),
                stats.getMotivoReclamo(),
                stats.getMotivoGarantia(),
                stats.getMotivoDuda(),
                stats.getMotivoCompra(),
                stats.getMotivoFelicitaciones(),
                stats.getMotivoCambio()
        );

        String expectedHash = DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
        log.info("RAW: {}", raw);
        log.info("Expected: {}", expectedHash);
        log.info("Received: {}", stats.getHash());
        return expectedHash.equals(stats.getHash());
    }
}
