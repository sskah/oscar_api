package br.com.fiap.oscar_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.oscar_api.model.Filme;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long>{
    
}
