package br.com.fiap.oscar_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FilmeResponse {
    private Long id;
    private String titulo;
    private String categoria;
    private BigDecimal orcamento;
    private String estudio;
}
