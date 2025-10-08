package com.pw.aerropuerto.service;


import com.pw.aerropuerto.api.dto.BookItemDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.repositories.BookItemRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.BookItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingItemServicelmpl implements BookingItemService {
    private final BookItemRepository repository;

    @Override
    public BookItemResponse create(BookItemCreateRequest req) {
        BookItem bookitemSave = repository.save(BookItemMapper.ToEntity(req));
        return BookItemMapper.ToResponse(bookitemSave);
    }
    @Override @Transactional(readOnly = true)
    public BookItemResponse get(Long id){
        return repository.findById(id).map(BookItemMapper::ToResponse).orElseThrow(() -> new NotFoundException("BookItem not found".formatted(id)));
    }
    @Override
    public Page<BookItemResponse> list(Pageable pageable){
        return repository.findAll(pageable).map(BookItemMapper::ToResponse);
    }
    @Override
    public BookItemResponse update(Long id, BookItemUpdateRequest req) {
        BookItem bookItem = repository.findById(id).orElseThrow(() -> new NotFoundException("BookItem not found".formatted(id)));
        if(req.cabinType() != null) bookItem.setCabin(Cabin.valueOf(req.cabinType()));
        if(req.price() != null) bookItem.setPrice(req.price());
        if(req.segmentOrder() != null) bookItem.setSegmentOrder(req.segmentOrder());
        BookItem update = repository.save(bookItem);
        return BookItemMapper.ToResponse(update);
    }
    @Override
    public void delete(Long id) {repository.deleteById(id);}
}
