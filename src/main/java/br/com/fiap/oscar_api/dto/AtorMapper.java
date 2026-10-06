package br.com.fiap.oscar_api.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.oscar_api.model.Ator;

@Component
public class AtorMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    // Para criar um DTO para persistir um objeto no BD
    public Ator toModel (AtorCreateRequest dto) {
        return modelMapper.map(dto, Ator.class);
    }

    // Para criar um DTO de resposta (do BD para o Controller)
    public AtorResponse toDto (Ator entity) {
        return modelMapper.map(entity, AtorResponse.class);
    }

    // Para criar um DTO para atualizar um objeto no BD
    public Ator toModel (Long id, AtorUpdateRequest dto) {
        Ator ator = modelMapper.map(dto, Ator.class);
        ator.setId(id);
        return ator;
    }
    
}
