package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.FeesDto;
import io.app.dto.PaymentRequest;
import io.app.services.impl.FeesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fees")
@RequiredArgsConstructor
public class FeesController {
    private final FeesServiceImpl service;


    @PostMapping("/student/{studentId}/batch/{batchId}/payment")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse recordPayment(@PathVariable("studentId") long studentId,
                                     @PathVariable("batchId") long batchId,
                                     @RequestBody PaymentRequest paymentRequest){
        return service.recordPayment(batchId,studentId,paymentRequest);
    }

    @GetMapping("/student/{studentId}/batch/{batchId}/fees")
    public ResponseEntity<FeesDto> getFees(
            @PathVariable("studentId") long studentId,
            @PathVariable("batchId") long batchId){
        return ResponseEntity.ok(service.getFeesByStudentAndBatch(studentId,batchId));
    }

}
