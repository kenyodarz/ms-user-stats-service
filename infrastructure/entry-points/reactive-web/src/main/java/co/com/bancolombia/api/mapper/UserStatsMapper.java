package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.request.UserStatsRequest;
import co.com.bancolombia.model.stats.UserStats;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserStatsMapper {
    public static UserStats toDomain(UserStatsRequest request) {
        return UserStats.builder()
                .totalContactoClientes(request.getTotalContactoClientes())
                .motivoReclamo(request.getMotivoReclamo())
                .motivoGarantia(request.getMotivoGarantia())
                .motivoDuda(request.getMotivoDuda())
                .motivoCompra(request.getMotivoCompra())
                .motivoFelicitaciones(request.getMotivoFelicitaciones())
                .motivoCambio(request.getMotivoCambio())
                .hash(request.getHash())
                .build();
    }
}

