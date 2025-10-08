package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.TagDtos.*;
import com.pw.aerropuerto.dominio.entities.Tag;
import com.pw.aerropuerto.dominio.repositories.TagRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    @Override
    public TagResponse create(TagCreateRequest request) {
        Tag tagSave = repository.save(TagMapper.ToEntity(request));
        return TagMapper.toResponse(tagSave);
    }

    @Override@Transactional(readOnly = true)
    public TagResponse get(Long id) {
        return repository.findById(id)
                .map(TagMapper::toResponse)
                .orElseThrow( () -> new NotFoundException("Tag %d not found".formatted(id)));
    }

    @Override@Transactional(readOnly = true)
    public List<TagResponse> List() {
        return repository.findAll()
                .stream().map(TagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse update(Long id, TagUpdateRequest request) {

        var u = repository.findById(id).orElseThrow( () -> new NotFoundException("Tag %d not found".formatted(id)));

        TagMapper.path(u, request);
        return TagMapper.toResponse(u);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }
}
