package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.BookItemDtos;
import com.pw.aerropuerto.api.dto.BookItemDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingItemService {
 BookItemResponse create (BookItemCreateRequest req);
 BookItemResponse get (Long id);
 Page<BookItemDtos> List (Pageable pageable);
 BookItemResponse update (Long id, BookItemUpdateRequest req);
 void delete (Long id);
}
