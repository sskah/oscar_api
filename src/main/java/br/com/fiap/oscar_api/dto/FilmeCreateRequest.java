package br.com.fiap.oscar_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FilmeCreateRequest {
    @NotNull
    @Size(min = 2, message = "O tamanho mínimo para o título é de 8 dígitos")
    private String titulo;
    private String categoria;
    private BigDecimal orcamento;
    private String estudio;
}
