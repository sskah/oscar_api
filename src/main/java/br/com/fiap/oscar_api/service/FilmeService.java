package br.com.fiap.oscar_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.oscar_api.model.Filme;
import br.com.fiap.oscar_api.repository.FilmeRepository;

@Service
public class FilmeService {
    
    @Autowired
    private FilmeRepository repository;

    // Insert ou Update
    public Filme createOrUpdate (Filme filme) {
        return repository.save(filme);
    }

    // Select *
    public List<Filme> findAll () {
        return repository.findAll();
    }

    // Select
    public Optional<Filme> findById (Long id) {
        return repository.findById(id);
    }

    // Delete
    public void deleteById (Long id) {
        repository.deleteById(id);
    }
}
