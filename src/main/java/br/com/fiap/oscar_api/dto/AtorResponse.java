package br.com.fiap.oscar_api.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
public class AtorResponse {
    private Long id;
    private String nome;
    private BigDecimal cache;
    private BigDecimal notaCritica;
    private Integer qtdIndicacoes;
}
