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
@Table (name = "filmes")
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column (length = 30, nullable = false)
    private String titulo;

    @Column(length = 30)
    private String categoria;

    @Column(length = 8, nullable = false)
    private BigDecimal orcamento;

    @Column(length = 50, nullable = false)
    private String estudio;
}
