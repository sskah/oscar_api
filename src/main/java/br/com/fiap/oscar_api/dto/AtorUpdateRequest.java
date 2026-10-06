package br.com.fiap.oscar_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AtorUpdateRequest {
    private String nome;
    private BigDecimal cache;
    private BigDecimal notaCritica;
    private Integer qtdIndicacoes;
}
