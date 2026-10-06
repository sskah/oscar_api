package br.com.fiap.oscar_api.controller;

import java.util.List;
import java.util.Optional;

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

import br.com.fiap.oscar_api.dto.AtorCreateRequest;
import br.com.fiap.oscar_api.dto.AtorMapper;
import br.com.fiap.oscar_api.dto.AtorResponse;
import br.com.fiap.oscar_api.dto.AtorUpdateRequest;
import br.com.fiap.oscar_api.model.Ator;
import br.com.fiap.oscar_api.repository.AtorRepository;
import br.com.fiap.oscar_api.service.AtorService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/${api.version}/atores")
public class AtorController {

    @Autowired
    private AtorService service;

    @Autowired
    private AtorMapper atorMapper;

    //Insert into
    @PostMapping("")
    public ResponseEntity<AtorResponse> create (@Valid @RequestBody AtorCreateRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atorMapper.toDto(service.createOrUpdate(atorMapper.toModel(dtoRequest))));
    }

    //Select *
    @GetMapping("")
    public ResponseEntity<List<AtorResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(ator -> atorMapper.toDto(ator)).toList());
    }

    //Select
    @GetMapping("/{id}")
    public ResponseEntity<AtorResponse> findById(@PathVariable Long id) {
        return service.findyById(id).map(ator -> atorMapper.toDto(ator)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<AtorResponse> update (@PathVariable Long id, @Valid @RequestBody AtorUpdateRequest dtoRequest) {

        if(service.findyById(id).isPresent()) {
            Ator atorAtualizado = atorMapper.toModel(id, dtoRequest);
            atorAtualizado.setId(id);
            return ResponseEntity.ok(atorMapper.toDto(service.createOrUpdate(atorAtualizado)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Long id) {
        if (service.findyById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
