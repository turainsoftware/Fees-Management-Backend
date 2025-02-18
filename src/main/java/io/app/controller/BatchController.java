package io.app.controller;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.BatchProjection;
import io.app.model.Batch;
import io.app.services.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
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

    @GetMapping("/analysis")
    public ResponseEntity<AnalysisResponse> analyse(
            @RequestHeader("Authorization") String authToken){
        System.out.println("inside batch analysis");
        return ResponseEntity.ok(service.batchAnalysis(authToken));
    }

    @GetMapping("/all-specific-details")
    public List<BatchProjection> specificBathesDetails(@RequestHeader("Authorization") String authToken){
        return service.getAllBatchDetailsWithSpecificDetails(authToken);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BatchDto batchById(@PathVariable("id") long id){
        return service.getBatchById(id);
    }

    @PatchMapping("/update-fees")
    public ApiResponse updateBatchFeesDetails(
            @RequestHeader("Authorization") String authToken,
            @RequestParam("batchId") long batchId,
            @RequestParam("monthlyFees") double monthlyFees,
            @RequestParam("monthlyExamFees") double monthlyExamFees){
        return service.updateBatchFees(authToken,batchId,monthlyFees,monthlyExamFees);
    }


}
