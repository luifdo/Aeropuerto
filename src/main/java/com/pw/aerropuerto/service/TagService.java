package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.TagDtos.*;

import java.util.List;

public interface TagService {

    TagResponse create(TagCreateRequest request);
    TagResponse get(Long id);
    List<TagResponse> List();
    TagResponse update(Long id,TagUpdateRequest request);
    void delete(Long id);

}
