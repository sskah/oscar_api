package br.com.fiap.oscar_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.oscar_api.dto.FilmeCreateRequest;
import br.com.fiap.oscar_api.dto.FilmeMapper;
import br.com.fiap.oscar_api.dto.FilmeResponse;
import br.com.fiap.oscar_api.dto.FilmeUpdateRequest;
import br.com.fiap.oscar_api.model.Filme;
import br.com.fiap.oscar_api.service.FilmeService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/${api.version}/filmes")
public class FilmeController {

    @Autowired
    private FilmeService service;

    @Autowired
    private FilmeMapper filmeMapper;

    //Insert into
    @PostMapping("")
    public ResponseEntity<FilmeResponse> create (@Valid @RequestBody FilmeCreateRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeMapper.toDto(service.createOrUpdate(filmeMapper.toModel(dtoRequest))));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<FilmeResponse>> findAll () {
        return ResponseEntity.ok(service.findAll().stream().map(filme -> filmeMapper.toDto(filme)).toList());
    }

    //Select
    @GetMapping("/{id}")
    public ResponseEntity<FilmeResponse> findById (@PathVariable Long id) {
        return service.findById(id).map(filme -> filmeMapper.toDto(filme)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<FilmeResponse> update (@PathVariable Long id, @Valid @RequestBody FilmeUpdateRequest dtoRequest) {

        if (service.findById(id).isPresent()) {
            Filme filmeAtualizado = filmeMapper.toModel(id, dtoRequest);
            filmeAtualizado.setId(id);
            return ResponseEntity.ok(filmeMapper.toDto(service.createOrUpdate(filmeAtualizado)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}