package br.com.fiap.oscar_api.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.oscar_api.model.Filme;

// Deixamos component pois ela só é uma classe que usamos para mapear dados (não chama interface, não fala com banco de dados, etc)
@Component
public class FilmeMapper {

    // O ModelMapper é uma biblioteca para mapeamento de objetos
    private final ModelMapper modelMapper = new ModelMapper();

    public Filme toModel (FilmeCreateRequest dto) {
        return modelMapper.map(dto, Filme.class);
    }

    public FilmeResponse toDto (Filme entity) {
        return modelMapper.map(entity, FilmeResponse.class);
    }

    public Filme toModel (Long id, FilmeUpdateRequest dto) {
        Filme filme = modelMapper.map(dto, Filme.class);
        filme.setId(id);
        return filme;
    }
}
