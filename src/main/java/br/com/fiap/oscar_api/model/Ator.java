package br.com.fiap.oscar_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "atores")
public class Ator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 10, nullable = false)
    private BigDecimal cache;

    @Column(length = 10, nullable = false)
    private BigDecimal notaCritica;

    @Column(nullable = false)
    private Integer qtdIndicacoes;
}
