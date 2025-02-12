package io.app.controller;

import io.app.dto.AnalysisResponse;
import io.app.dto.TeacherFeesHistoryDto;
import io.app.services.impl.FeesHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fees-history")
@RequiredArgsConstructor
public class FeesHistoryController {
    private final FeesHistoryServiceImpl service;

    @GetMapping("/teacher")
    public ResponseEntity<List<TeacherFeesHistoryDto>> getFeesHistoryByTeacher(
            @RequestHeader("Authorization") String authToken
    ){
        return ResponseEntity.ok(service.feesByTeacher(authToken));
    }

    @GetMapping("/teacher/latest")
    public ResponseEntity<List<TeacherFeesHistoryDto>> getLatestFeesHistoryByTeacher(
            @RequestHeader("Authorization") String authToken
    ){
        return ResponseEntity.ok(service.latestFeesByTeacher(authToken));
    }

    @GetMapping("/teacher/range")
    public ResponseEntity<List<TeacherFeesHistoryDto>> getRangeFeesHistory(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(value = "pageNo",defaultValue = "0") int pageNo,
            @RequestParam(value = "size",defaultValue = "10") int size){
        return ResponseEntity.ok(service.feesByTeacherInRange(authToken,pageNo,size));
    }

    @GetMapping("/analysis")
    public ResponseEntity<AnalysisResponse> getMonthlyAnalysis(
            @RequestHeader("Authorization") String authToken
    ){
        return ResponseEntity.ok(service.feesAnalysisByTeacherAndMonths(authToken));
    }

}