package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;

public interface BatchService {
    public ApiResponse createBatch(String authToken, BatchDto batchDto);


    public String extractJwt(String authToken);

}
