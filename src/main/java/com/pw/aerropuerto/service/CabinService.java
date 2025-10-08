package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.CabinDtos;
import com.pw.aerropuerto.api.dto.CabinDtos.*;

import java.util.List;

public interface CabinService {
CabinResponse create(CabinCreateRequest req);
List<CabinResponse> List();
}

