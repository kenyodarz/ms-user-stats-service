package co.com.bancolombia.dynamodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class UserStatsEntity {

    private String timestamp;
    private Integer totalContactoClientes;
    private Integer motivoReclamo;
    private Integer motivoGarantia;
    private Integer motivoDuda;
    private Integer motivoCompra;
    private Integer motivoFelicitaciones;
    private Integer motivoCambio;
    private String hash;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @DynamoDbAttribute("totalContactoClientes")
    public Integer getTotalContactoClientes() {
        return totalContactoClientes;
    }

    @DynamoDbAttribute("motivoReclamo")
    public Integer getMotivoReclamo() {
        return motivoReclamo;
    }

    @DynamoDbAttribute("motivoGarantia")
    public Integer getMotivoGarantia() {
        return motivoGarantia;
    }

    @DynamoDbAttribute("motivoDuda")
    public Integer getMotivoDuda() {
        return motivoDuda;
    }

    @DynamoDbAttribute("motivoCompra")
    public Integer getMotivoCompra() {
        return motivoCompra;
    }

    @DynamoDbAttribute("motivoFelicitaciones")
    public Integer getMotivoFelicitaciones() {
        return motivoFelicitaciones;
    }

    @DynamoDbAttribute("motivoCambio")
    public Integer getMotivoCambio() {
        return motivoCambio;
    }

    @DynamoDbAttribute("hash")
    public String getHash() {
        return hash;
    }

}
