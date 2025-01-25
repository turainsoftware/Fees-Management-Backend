package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.model.Batch;
import io.app.services.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createBatch(@RequestBody BatchDto batchDto,
                                   @RequestHeader("Authorization") String authToken){
        return service.createBatch(authToken,batchDto);
    }

    @GetMapping
    public List<BatchDto> getAllBatch(@RequestHeader("Authorization") String authToken){
        return service.getAllBatch(authToken);
    }

}
