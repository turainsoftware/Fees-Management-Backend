package io.app.controller;

import io.app.dto.TeacherFeesHistoryDto;
import io.app.services.impl.FeesHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

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

}
