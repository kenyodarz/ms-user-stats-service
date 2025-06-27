package co.com.bancolombia.api.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserStatsRequest {
    @NotNull
    private Integer totalContactoClientes;
    @NotNull
    private Integer motivoReclamo;
    @NotNull
    private Integer motivoGarantia;
    @NotNull
    private Integer motivoDuda;
    @NotNull
    private Integer motivoCompra;
    @NotNull
    private Integer motivoFelicitaciones;
    @NotNull
    private Integer motivoCambio;
    @NotBlank
    private String hash;
}
